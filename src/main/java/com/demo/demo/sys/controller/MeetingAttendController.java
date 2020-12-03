package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.MeetingAttend;
import com.demo.demo.sys.entity.UserGroupJobNumber;
import com.demo.demo.sys.entity.UserLeader;
import com.demo.demo.sys.result.RetUserInfo;
import com.demo.demo.sys.service.impl.MeetingAttendServiceImpl;
import com.demo.demo.sys.service.impl.UserGroupJobNumberServiceImpl;
import com.demo.demo.sys.service.impl.UserLeaderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-05
 */
@Controller
@RequestMapping("/meetingAttend")
@Slf4j
public class MeetingAttendController {

    @Autowired
    MeetingAttendServiceImpl meetingAttendService;
    @Autowired
    UserGroupJobNumberServiceImpl userGroupJobNumberService;
    @Autowired
    UserLeaderServiceImpl userLeaderService;
    /**
     * 根据会议Id 获取会议的列表
     *
     * @param meetingId 会议Id
     * @return
     */
    @GetMapping("/getGroupUserList")
    @ResponseBody
    public RetResult<List<RetUserInfo>> getGroupUserList(Integer meetingId){
        List<RetUserInfo> retUserInfoList = new ArrayList<>();
        QueryWrapper<MeetingAttend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("meeting_id",meetingId);
        List<MeetingAttend> list = meetingAttendService.list(queryWrapper);
        for(MeetingAttend meetingAttend :list){
            Employee employee = UserInfoUtil.getEmployeeByIdFromServer(meetingAttend.getJobNumber());
            if(employee != null){
                RetUserInfo retUserInfo = new RetUserInfo();
                retUserInfo.setUserName(employee.getUserName());
                retUserInfo.setId(meetingAttend.getId());
                retUserInfo.setJobNumber(meetingAttend.getJobNumber());
                retUserInfo.setDepartmentName(employee.getDepartment());
                retUserInfoList.add(retUserInfo);
            }else {
                UserLeader userLeader = userLeaderService.getUserLeaderListByJobNumber(meetingAttend.getJobNumber());
                if(userLeader != null){
                    RetUserInfo retUserInfo = new RetUserInfo();
                    retUserInfo.setUserName(userLeader.getName());
                    retUserInfo.setId(meetingAttend.getId());
                    retUserInfo.setJobNumber(meetingAttend.getJobNumber());
                    retUserInfo.setDepartmentName(userLeader.getDepartmentName());
                    retUserInfoList.add(retUserInfo);
                }else {
                    RetUserInfo retUserInfo = new RetUserInfo();
                    retUserInfo.setUserName("未知用户");
                    retUserInfo.setId(meetingAttend.getId());
                    retUserInfo.setJobNumber(meetingAttend.getJobNumber());
                    retUserInfo.setDepartmentName("未知用户");
                    retUserInfoList.add(retUserInfo);
                }
            }



        }
        return RetResponse.makeOKRsp(retUserInfoList);
    }


    /**
     * 添加一个参会人员
     *
     * @param meetingId 会议ID
     * @param jobNumber 用户工号
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<Boolean> add(Integer meetingId,String jobNumber){
        QueryWrapper<MeetingAttend> meetingAttendQueryWrapper = new QueryWrapper<>();
        meetingAttendQueryWrapper.eq("meeting_id",meetingId)
                .eq("job_number",jobNumber);
        MeetingAttend meetingAttend = meetingAttendService.getOne(meetingAttendQueryWrapper);
        if(meetingAttend != null){
            return RetResponse.makeErrRsp("用户已经在数据库中");
        }else {
            MeetingAttend meetingAttendNew = new MeetingAttend();
            meetingAttendNew.setMeetingId(meetingId);
            meetingAttendNew.setJobNumber(jobNumber);
            return RetResponse.makeOKRsp(meetingAttendService.save(meetingAttendNew));
        }

    }

    /**
     * 添加一个参会人员
     *
     * @param meetingId 会议ID
     * @param groupId 群组Id
     * @return
     */
    @PostMapping("/addByGroup")
    @ResponseBody
    public RetResult<Boolean> addByGroup(Integer meetingId,String groupId){
        // 根据groupId 群组内全部用户工号 jobNumber
        QueryWrapper<UserGroupJobNumber> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_group_id",groupId)
                .eq("alive",0);
        List<UserGroupJobNumber> userGroupJobNumbers = userGroupJobNumberService.list(queryWrapper);
        for(UserGroupJobNumber userGroupJobNumber : userGroupJobNumbers){
            QueryWrapper<MeetingAttend> meetingAttendQueryWrapper = new QueryWrapper<>();
            meetingAttendQueryWrapper.eq("meeting_id",meetingId)
                    .eq("job_number",userGroupJobNumber.getJobNumber());
            MeetingAttend meetingAttend = meetingAttendService.getOne(meetingAttendQueryWrapper);
            if(meetingAttend != null){
                log.info(userGroupJobNumber.getJobNumber()+"已经在参会列表中");
            }else {
                MeetingAttend meetingAttendNew = new MeetingAttend();
                meetingAttendNew.setMeetingId(meetingId);
                meetingAttendNew.setJobNumber(userGroupJobNumber.getJobNumber());
                Boolean flag = meetingAttendService.save(meetingAttendNew);
                if(flag){
                    log.info(userGroupJobNumber.getJobNumber()+"添加成功");
                }else {
                    log.info(userGroupJobNumber.getJobNumber()+"添加失败");
                }
            }
        }
        return RetResponse.makeOKRsp();
    }



    /**
     *  批量删除
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(@RequestBody List<Integer> ids){
        Boolean flag = false;
        for(Integer id :ids){
            flag = meetingAttendService.removeById(id);
        }
        return RetResponse.makeOKRsp(flag);
    }


}
