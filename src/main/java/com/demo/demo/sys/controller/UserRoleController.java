package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.common.SystemCommon;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.UserInfo;
import com.demo.demo.sys.entity.UserRole;
import com.demo.demo.sys.result.RetUserInfo;
import com.demo.demo.sys.service.impl.UserRoleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
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
@RequestMapping("/userRole")
@Slf4j
public class UserRoleController {
    @Autowired
    UserRoleServiceImpl userRoleService;

    /**
     *  添加会议创建者
     * @param jobNumber
     * @return
     */
    @ResponseBody
    @PostMapping("/add")
    public RetResult<String> add(String jobNumber){
        //String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL + jobNumber);
        Employee result = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if (result == null) {
            return RetResponse.makeErrRsp("用户不存在");
        } else {
            // 如果数据库存在 无法添加
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_number",jobNumber);
            UserRole userRole = userRoleService.getOne(queryWrapper);
            if(userRole != null){
                return RetResponse.makeErrRsp("工号为"+jobNumber+"用户已是会议创建者");
            }else {
                UserRole userRole1 = new UserRole();
                userRole1.setJobNumber(jobNumber);
                //
                userRole1.setRoleId(2);
                userRole1.setAlive(0);
                userRoleService.save(userRole1);
                return RetResponse.makeOKRsp("添加成功！");
            }
        }
    }

    @ResponseBody
    @GetMapping("/getList")
    public RetResult<List<RetUserInfo>> getList(){
        List<UserRole> userRoleList = userRoleService.list();
        List<RetUserInfo> retUserInfos = new ArrayList<>();
        for(UserRole userRole : userRoleList){
            RetUserInfo retUserInfo = new RetUserInfo();
            //String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL+userRole.getJobNumber());
            Employee result = UserInfoUtil.getEmployeeByIdFromServer(userRole.getJobNumber());
            retUserInfo.setUserName(result.getUserName());
            retUserInfo.setJobNumber(result.getJobNumber());
            retUserInfo.setDepartmentName(result.getDepartment());
            retUserInfo.setId(userRole.getId());
            retUserInfos.add(retUserInfo);
        }
        return RetResponse.makeOKRsp(retUserInfos);
    }

    /**
     *  批量删除
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Integer> del(@RequestBody List<Integer> ids){
        for(Integer id :ids){
            log.info("freeIpService.removeById(id)"+userRoleService.removeById(id));
        }
        return RetResponse.makeOKRsp(1);
    }

}
