package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.sys.entity.MeetingLeader;
import com.demo.demo.sys.entity.UserLeader;
import com.demo.demo.sys.service.impl.MeetingLeaderServiceImpl;
import com.demo.demo.sys.service.impl.UserLeaderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-07-15
 */
@Controller
@RequestMapping("/meetingLeader")
@Slf4j
public class MeetingLeaderController {
    @Autowired
    MeetingLeaderServiceImpl meetingLeaderService;
    @Autowired
    UserLeaderServiceImpl userLeaderService;

    /**
     * 根据用户工号获取领导列表
     *
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<MeetingLeader>> getList(Integer meetingId){
        QueryWrapper<MeetingLeader> userLeaderQueryWrapper = new QueryWrapper<>();
        userLeaderQueryWrapper.eq("alive",0)
                .eq("meeting_id",meetingId);
        return  RetResponse.makeOKRsp(meetingLeaderService.list(userLeaderQueryWrapper));
    }

    /**
     * 添加一个领导
     *
     * @param meetingLeader 领导信息
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<Boolean> add(MeetingLeader meetingLeader){
        meetingLeader.setAlive(0);
        Boolean result = meetingLeaderService.save(meetingLeader);
        return  RetResponse.makeOKRsp(result);
    }

    /**
     *  批量删除
     *
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(@RequestBody List<Integer> ids,Integer meetingId){
        System.out.println(meetingId);
       /* log.info(ids.toArray().toString());
        Boolean flag = false;
        for(Integer id :ids){
            flag = meetingLeaderService.removeById(id);
        }*/
        return RetResponse.makeOKRsp(true);
    }


    @PostMapping("/delById")
    @ResponseBody
    public RetResult<Boolean> del(Integer id){
        System.out.println(id);
        return RetResponse.makeOKRsp(meetingLeaderService.removeById(id));
    }

    @PostMapping("/reset")
    @ResponseBody
    public RetResult<Boolean> reset(Integer meetingId){

        // 1. 删除会议全部领导
        QueryWrapper<MeetingLeader> meetingLeaderQueryWrapper = new QueryWrapper<>();
        meetingLeaderQueryWrapper.eq("alive",0)
                .eq("meeting_id",meetingId);
        List<MeetingLeader> meetingLeaderDelList = meetingLeaderService.list(meetingLeaderQueryWrapper);
        for (MeetingLeader meetingLeader : meetingLeaderDelList){
            meetingLeaderService.removeById(meetingLeader.getId());
        }
        // 2. 获取全部领导
        String jobNumber = SecurityContextHolder.getContext().getAuthentication().getName();
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
        return RetResponse.makeOKRsp(true);
    }







}
