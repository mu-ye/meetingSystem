package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.sys.entity.UserGroup;
import com.demo.demo.sys.service.impl.UserGroupServiceImpl;
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
 * @since 2020-05-26
 */
@Controller
@RequestMapping("/group")
@Slf4j
public class UserGroupController {

    @Autowired
    UserGroupServiceImpl userGroupService;

    /**
     *  新建群组
     * @param groupName 群组名字
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<String> add(String groupName){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();

        QueryWrapper<UserGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",groupName);
        if(!userGroupService.list(queryWrapper).isEmpty()){
            return RetResponse.makeErrRsp("群组已存在，请重新输入群组名称");
        }else {
            UserGroup userGroup = new UserGroup();
            userGroup.setUserJobNumber(jobNumber);
            userGroup.setAlive(0);
            userGroup.setName(groupName);
            userGroupService.save(userGroup);
            return RetResponse.makeOKRsp("创建成功");
        }
    }

    /**
     * 根据用户工号获取全部群组
     *
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<UserGroup>> add(){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<UserGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_job_number",jobNumber)
                .eq("alive",0);
        return RetResponse.makeOKRsp(userGroupService.list(queryWrapper));
    }

    /**
     *  批量删除
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(@RequestBody List<Integer> ids){
        log.info(ids.toArray().toString());
        Boolean flag = false;
        for(Integer id :ids){
            flag = userGroupService.removeById(id);
        }
        return RetResponse.makeOKRsp(flag);
    }


    /**
     * 群组重命名
     *
     * @param id 群组Id
     * @param name 群组新名字
     * @return
     */
    @PostMapping("/reName")
    @ResponseBody
    public RetResult<Boolean> reName(Integer id , String name){
        UserGroup userGroup = userGroupService.getById(id);
        userGroup.setName(name);
        return RetResponse.makeOKRsp(userGroupService.saveOrUpdate(userGroup));
    }

    /**
     * 根据用户Id 获取一条记录
     *
     * @param id
     * @return
     */
    @GetMapping("/getOne")
    @ResponseBody
    public RetResult<UserGroup> getOne(Integer id){
        return RetResponse.makeOKRsp(userGroupService.getById(id));
    }




}
