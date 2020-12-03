package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.UserGroupJobNumber;
import com.demo.demo.sys.result.RetUserInfo;
import com.demo.demo.sys.service.impl.UserGroupJobNumberServiceImpl;
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
 * @since 2020-05-26
 */
@Controller
@RequestMapping("/groupUser")
@Slf4j
public class UserGroupJobNumberController {
    @Autowired
    UserGroupJobNumberServiceImpl userGroupJobNumberService;


    /**
     * 根据群组ID 获取群组内全部人员信息
     *
     * @param groupId 群组Id
     * @return
     */
    @GetMapping("/getGroupUserList")
    @ResponseBody
    public RetResult<List<RetUserInfo>> getGroupUserList(Integer groupId){
        List<RetUserInfo> retUserInfoList = new ArrayList<>();
        QueryWrapper<UserGroupJobNumber> userGroupJobNumberQueryWrapper = new QueryWrapper<>();
        userGroupJobNumberQueryWrapper.eq("alive",0)
                .eq("user_group_id",groupId);
        List<UserGroupJobNumber> list = userGroupJobNumberService.list(userGroupJobNumberQueryWrapper);
        for(UserGroupJobNumber userGroupJobNumber :list){
            Employee employee = UserInfoUtil.getEmployeeByIdFromServer(userGroupJobNumber.getJobNumber());
            RetUserInfo retUserInfo = new RetUserInfo();
            retUserInfo.setUserName(employee.getUserName());
            retUserInfo.setId(userGroupJobNumber.getId());
            retUserInfo.setJobNumber(userGroupJobNumber.getJobNumber());
            retUserInfo.setDepartmentName(employee.getDepartment());
            retUserInfoList.add(retUserInfo);
        }
        return RetResponse.makeOKRsp(retUserInfoList);
    }

    /**
     * 根据群组ID 获取群组内全部人员信息
     *
     * @param groupId 群组Id
     * @return
     */
    @GetMapping("/getGroupUserString")
    @ResponseBody
    public RetResult<String> getGroupUserString(Integer groupId){
        List<RetUserInfo> retUserInfoList = new ArrayList<>();
        QueryWrapper<UserGroupJobNumber> userGroupJobNumberQueryWrapper = new QueryWrapper<>();
        userGroupJobNumberQueryWrapper.eq("alive",0)
                .eq("user_group_id",groupId);
        List<UserGroupJobNumber> list = userGroupJobNumberService.list(userGroupJobNumberQueryWrapper);
        StringBuffer stringBuffer = new StringBuffer();

        for(UserGroupJobNumber userGroupJobNumber :list){
            Employee employee = UserInfoUtil.getEmployeeByIdFromServer(userGroupJobNumber.getJobNumber());
            stringBuffer.append(employee.getJobNumber());
            stringBuffer.append(":");
            stringBuffer.append(employee.getUserName());
            stringBuffer.append(";");
        }
        return RetResponse.makeOKRsp(stringBuffer.toString());
    }

    /**
     *
     * @param groupId 群组Id
     * @param jobNumber 添加用户工号
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<Boolean> add(Integer groupId,String jobNumber){
        UserGroupJobNumber userGroupJobNumber = new UserGroupJobNumber();
        userGroupJobNumber.setUserGroupId(groupId);
        userGroupJobNumber.setJobNumber(jobNumber);
        userGroupJobNumber.setAlive(0);
        return RetResponse.makeOKRsp(userGroupJobNumberService.save(userGroupJobNumber));

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
            flag = userGroupJobNumberService.removeById(id);
        }
        return RetResponse.makeOKRsp(flag);
    }


    /**
     * 根据群组ID 获取群组内全部人员信息
     *
     * @param groupId 群组Id
     * @return
     */
    @GetMapping("/getUpLoadUsersByGroup")
    @ResponseBody
    public RetResult<String> getUpLoadUsersByGroup(Integer groupId){
        List<RetUserInfo> retUserInfoList = new ArrayList<>();
        QueryWrapper<UserGroupJobNumber> userGroupJobNumberQueryWrapper = new QueryWrapper<>();
        userGroupJobNumberQueryWrapper.eq("alive",0)
                .eq("user_group_id",groupId);
        List<UserGroupJobNumber> list = userGroupJobNumberService.list(userGroupJobNumberQueryWrapper);
        StringBuffer stringBuffer = new StringBuffer();

        for(UserGroupJobNumber userGroupJobNumber :list){
            Employee employee = UserInfoUtil.getEmployeeByIdFromServer(userGroupJobNumber.getJobNumber());
            stringBuffer.append(",");
            stringBuffer.append("(");
            stringBuffer.append(employee.getJobNumber());
            stringBuffer.append(")");
            stringBuffer.append(employee.getUserName());
        }
        return RetResponse.makeOKRsp(stringBuffer.toString());
    }
}
