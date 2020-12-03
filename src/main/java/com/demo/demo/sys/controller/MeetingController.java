package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.RandomString;
import com.demo.demo.sys.entity.*;
import com.demo.demo.sys.service.IMeetingFileService;
import com.demo.demo.sys.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-04
 */
@Controller
@RequestMapping("/meeting")
@Slf4j
public class MeetingController {

    @Autowired
    MeetingServiceImpl meetingService;
    @Autowired
    TemplateMenuServiceImpl templateMenuService;
    @Autowired
    MeetingMenuServiceImpl meetingMenuService;
    @Autowired
    DepartmentServiceImpl departmentService;
    @Autowired
    IMeetingFileService meetingFileService;

    @Autowired
    MeetingLeaderServiceImpl meetingLeaderService;
    @Autowired
    UserLeaderServiceImpl userLeaderService;


    @PostMapping("/getEndTime")
    @ResponseBody
    public RetResult<String> getEndTime(Integer meetingId){
        Meeting meeting = meetingService.getById(meetingId);

        DateTimeFormatter showFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(meeting!=null){
            log.info(showFormatter.format(meeting.getEndData()));
            return RetResponse.makeOKRsp(showFormatter.format(meeting.getEndData()));
        }else {
            return RetResponse.makeOKRsp("截至时间返回错误");
        }

    }


    /**
     * 获取参会会议全部部门
     *
     * @param meetingId 会议Id
     * @return
     */
    @GetMapping("/getAttendDepartment")
    @ResponseBody
    public RetResult<String> getAttendDepartment(Integer meetingId){
        List<String> departmentList = meetingFileService.iGetAttendDepartment(meetingId);
        StringBuffer buffer = new StringBuffer();
        int[] department  = new int[30];
        for (String str : departmentList){
            String[] item = str.split(",");
            for(String index: item){
                Integer intIndex = Integer.parseInt(index);
                System.out.println("intIndex"+intIndex);
                department[intIndex] = 1;
            }
        }
        for(int i=0; i<department.length;i++){

            if(department[i] == 1){
                System.out.println("i"+i);
                buffer.append(departmentService.getById(i).getDepartmentName());
                buffer.append(" , ");
            }
        }
        return RetResponse.makeOKRsp(buffer.toString());
    }

    /**
     * 根据会议Id获取会议文件上传截至时间，超过当前时间不允许文件上传
     *
     * @param meetingId 会议Id
     * @return
     */
    @PostMapping("/getCanFile")
    @ResponseBody
    public RetResult<Boolean> getCanFile(Integer meetingId){
        Meeting meeting = meetingService.getById(meetingId);
        LocalDateTime fileUpEndTime = meeting.getEndData();
        if(fileUpEndTime.isBefore(LocalDateTime.now())){
            log.info("文件上传已截至");
            return RetResponse.makeOKRsp(false);
        }else {
            log.info("文件上传未截至");
            return RetResponse.makeOKRsp(true);
        }
    }

    /**
     * 新建会议
     *
     * @param meeting 用meeting实体来接收一些简单表单内容
     * @param endDate 截至时间
     * @param startDate 开始时间
     * @return
     */
    @PostMapping("/addMeeting")
    public String addMeeting(Meeting meeting, @RequestParam("endDate1") String endDate, @RequestParam("startDate1") String startDate){
        // 会议名称 + "$$$" + 当前时间作为唯一名称
        meeting.setName(meeting.getName()+"==="+LocalDateTime.now().toString());

        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        if(!endDate.equals("")){
            meeting.setEndData(LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(!startDate.equals("")){
            meeting.setStartDate(LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        QueryWrapper<Meeting> meetingQueryWrapper = new QueryWrapper<>();
        meetingQueryWrapper.eq("name",meeting.getName());
        Meeting meeting1 = meetingService.getOne(meetingQueryWrapper);
        if(meeting1 != null){
            meeting.setName(meeting.getName()+"(会议名重复，请重命名"+RandomString.getRandomString(6)+")");
        }
        meeting.setAlive(0);

        // 默认创建的会议对前台是关闭的
        meeting.setState(1);
        if(meeting.getAttendPassword().equals("Y")){
            // 允许使用会议密码，设置四位数的会议密码
            meeting.setAttendPassword(RandomString.getRandomString(6));
        }
        meeting.setJobNumber(jobNumber);
        meetingService.save(meeting);
        log.info("---------------------会议创建成功---------------------------");
        // 获取新建会议的会议Id
        meetingQueryWrapper.eq("name",meeting.getName());
        QueryWrapper<Meeting> meetingQueryWrapper1 = new QueryWrapper<>();
        meetingQueryWrapper1.eq("name",meeting.getName());
        Integer meetingId = meetingService.getOne(meetingQueryWrapper1).getId();
        log.info("新建会议ID "+meetingId);

        // 添加参会领导
        QueryWrapper<UserLeader> userLeaderQueryWrapper = new QueryWrapper<>();
        userLeaderQueryWrapper.eq("creat_job_number",jobNumber)
                .eq("alive",0);
        List<UserLeader> userLeaderList = userLeaderService.list(userLeaderQueryWrapper);
        for (UserLeader userLeader : userLeaderList){
            MeetingLeader meetingLeader = new MeetingLeader();
            meetingLeader.setAlive(0);
            meetingLeader.setMeetingId(meetingId);
            meetingLeader.setDepartmentName(userLeader.getDepartmentName());
            meetingLeader.setLeaderJobNumber(userLeader.getLeaderJobNumber());
            meetingLeader.setName(userLeader.getName());
            meetingLeaderService.save(meetingLeader);
        }

        QueryWrapper<TemplateMenu> templateMenuQueryWrapper = new QueryWrapper<>();
        templateMenuQueryWrapper.eq("template_id",meeting.getTemplateId())
                .eq("parent_id",0);
        List<TemplateMenu> templateMenuList = templateMenuService.list(templateMenuQueryWrapper);
        for(TemplateMenu templateMenu : templateMenuList){
            MeetingMenu meetingMenu = new MeetingMenu(templateMenu,meetingId);
            meetingMenuService.save(meetingMenu);
        }
        log.info("--------------------第一层目录拷贝成功--------------------------------");
        QueryWrapper<TemplateMenu> templateMenuQueryWrapper1 = new QueryWrapper<>();
        templateMenuQueryWrapper1.eq("template_id",meeting.getTemplateId())
                .gt("parent_id",0);
        List<TemplateMenu> templateMenuListOther = templateMenuService.list(templateMenuQueryWrapper1);
        log.info("--------------------获取第二层循环--------------------------------");
        for(TemplateMenu templateMenu : templateMenuListOther){
            // 根据templateMenu->parentId -> text  再根据text和meetingId在meeting_menu中获取id 作为父Id
            String parentText = templateMenuService.getById(templateMenu.getParentId()).getText();
            QueryWrapper<MeetingMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("meeting_id",meetingId)
                    .eq("text",parentText);
            Integer parentId = meetingMenuService.getOne(queryWrapper).getId();
            templateMenu.setParentId(parentId);
            MeetingMenu meetingMenu = new MeetingMenu(templateMenu,meetingId);
            meetingMenuService.save(meetingMenu);
        }
        return "redirect:/index/manageMeeting";
    }

    /**
     *  编辑会议
     *
     * @param meeting 用meeting实体来接收一些简单表单内容
     * @param endDate 截至时间
     * @param startDate 开始时间
     * @return
     */
    @PostMapping("/editMeeting")
    public String editMeeting(Meeting meeting, @RequestParam("endDate1") String endDate, @RequestParam("startDate1") String startDate){
        // 会议名称 + "$$$" + 当前时间作为唯一名称
        meeting.setName(meeting.getName()+"==="+LocalDateTime.now().toString());

        meeting.setTemplateId(meetingService.getById(meeting.getId().toString()).getTemplateId());
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        if(!endDate.equals("")){
            meeting.setEndData(LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        if(!startDate.equals("")){
            meeting.setStartDate(LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        QueryWrapper<Meeting> meetingQueryWrapper = new QueryWrapper<>();
        meetingQueryWrapper.eq("name",meeting.getName());
        Meeting meeting1 = meetingService.getOne(meetingQueryWrapper);
        if(meeting1 != null){
            meeting.setName(meeting.getName()+"(会议名重复，请重命名"+RandomString.getRandomString(6)+")");
        }
        meeting.setAlive(0);
        // 默认创建的会议对前台是开启的
        meeting.setState(0);
        if(meeting.getAttendPassword().equals("Y")){
            // 允许使用会议密码，设置四位数的会议密码
            meeting.setAttendPassword(RandomString.getRandomString(6));
        }
        meeting.setJobNumber(jobNumber);
        meetingService.saveOrUpdate(meeting);
        return "redirect:/index/editMeeting";
    }

    /**
     * 我的会议列表--我创建的会议（时间没有要求）
     *
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<Meeting>> getList(){
        List<Meeting> meetingList = meetingService.iGetMeetingsToDo();
        for(Meeting meeting : meetingList){
            // 截取会议名字 eg : 招标采购会议$$$当前时间 -》 截取为  招标采购会议
            meeting.setName(meeting.getName().split("===")[0]);
        }
        return RetResponse.makeOKRsp(meetingList);
    }

    /**
     * 历史会议列表 alive = 1
     *
     * @return
     */
    @GetMapping("/getHistoryList")
    @ResponseBody
    public RetResult<List<Meeting>> getHistoryList(){
        List<Meeting> meetingList = meetingService.iGetMeetingsHistory();
        for(Meeting meeting : meetingList){
            // 截取会议名字 eg : 招标采购会议$$$当前时间 -》 截取为  招标采购会议
            meeting.setName(meeting.getName().split("===")[0]);
        }
        return RetResponse.makeOKRsp(meetingList);
    }

    /**
     * 批量删除
     *
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(@RequestBody List<Integer> ids){
        for(Integer id :ids){
            // 删除会议
            meetingService.removeById(id);
            // 删除会议对应的目录结构
            QueryWrapper<MeetingMenu> meetingMenuQueryWrapper = new QueryWrapper<>();
            meetingMenuQueryWrapper.eq("meeting_id",id);
            List<MeetingMenu> meetingMenuList = meetingMenuService.list(meetingMenuQueryWrapper);
            if(! meetingMenuList.isEmpty()){
                for(MeetingMenu meetingMenu :meetingMenuList){
                    meetingMenuService.removeById(meetingMenu.getId());
                }
            }
        }
        return RetResponse.makeOKRsp(true);
    }

    /**
     *  会议重命名
     * @param id 模板ID
     * @param name 模板名称
     * @return
     */
    @PostMapping("/rename")
    @ResponseBody
    public RetResult<Boolean> rename(Integer id,String name){
        Meeting meeting = meetingService.getById(id);
        meeting.setName(name);
        return RetResponse.makeOKRsp(meetingService.saveOrUpdate(meeting));
    }

    /**
     * 根据ID 获取会议详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getOneById")
    @ResponseBody
    public RetResult<Meeting> getOneById(Integer id){
        Meeting meeting = meetingService.getById(id);
        meeting.setName(meeting.getName().split("===")[0]);
        return RetResponse.makeOKRsp(meeting);
    }

    /**
     * 开启会议
     *
     * @param id 会议ID
     * @return booLean true : 成功  false : 失败
     */
    @GetMapping("/openMeeting")
    @ResponseBody
    public RetResult<Boolean> openMeeting(Integer id){
        Meeting meeting = meetingService.getById(id);
        meeting.setState(0);
        Boolean flag = meetingService.saveOrUpdate(meeting);
        return RetResponse.makeOKRsp(flag);
    }

    /**
     * 关闭会议
     *
     * @param id 会议ID
     * @return booLean true : 成功  false : 失败
     */
    @GetMapping("/closeMeeting")
    @ResponseBody
    public RetResult<Boolean> closeMeeting(Integer id){
        Meeting meeting = meetingService.getById(id);
        meeting.setState(1);
        Boolean flag = meetingService.saveOrUpdate(meeting);
        return RetResponse.makeOKRsp(flag);
    }


    /**
     * 获取 允许上传文件会议列表
     *
     * @return
     */
    @GetMapping("/getMeetingListUpload")
    @ResponseBody
    public RetResult<List<Meeting>> getMeetingListUpload(){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        List<Meeting> meetingList = meetingMenuService.iGetMeetingListUpload(jobNumber);
        for(Meeting meeting : meetingList){
            // 截取会议名字 eg : 招标采购会议$$$当前时间 -》 截取为  招标采购会议
            meeting.setName(meeting.getName().split("===")[0]);
        }
        return RetResponse.makeOKRsp(meetingList);
    }

}
