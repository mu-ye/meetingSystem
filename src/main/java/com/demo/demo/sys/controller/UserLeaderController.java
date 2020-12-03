package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.sys.entity.UserLeader;
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
@RequestMapping("/userLeader")
@Slf4j
public class UserLeaderController {
    @Autowired
    UserLeaderServiceImpl userLeaderService;

    /**
     * 根据用户工号获取领导列表
     *
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<UserLeader>> getList(){
        String jobNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<UserLeader> userLeaderQueryWrapper = new QueryWrapper<>();
        userLeaderQueryWrapper.eq("creat_job_number",jobNumber)
                .eq("alive",0);
        return  RetResponse.makeOKRsp(userLeaderService.list(userLeaderQueryWrapper));
    }

    /**
     * 添加一个领导
     *
     * @param userLeader 领导信息
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<Boolean> add(UserLeader userLeader){
        String jobNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        userLeader.setAlive(0);
        userLeader.setCreatJobNumber(jobNumber);
        Boolean result = userLeaderService.save(userLeader);
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
    public RetResult<Boolean> del(@RequestBody List<Integer> ids){
        log.info(ids.toArray().toString());
        Boolean flag = false;
        for(Integer id :ids){
            flag = userLeaderService.removeById(id);
        }
        return RetResponse.makeOKRsp(flag);
    }




}
