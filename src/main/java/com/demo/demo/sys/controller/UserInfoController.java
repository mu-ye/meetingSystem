package com.demo.demo.sys.controller;


import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-05-26
 */
@Controller
@RequestMapping("/userInfo")
@Slf4j
public class UserInfoController {

    @ResponseBody
    @PostMapping("/getUserInfo")
    public RetResult<String> getUserInfo(String jobNumber) {
        /*String result = UserInfoUtil.get(SystemCommon.BASE_USER_INFO_URL + jobNumber);
        if (result == null) {
            return RetResponse.makeErrRsp("用户不存在，请核对工号后再次查询");
        } else {
            JSONObject jsonUserInfo = JSONObject.fromObject(result);
            StringBuilder builder = new StringBuilder();
            builder.append("用户工号 ：");
            builder.append(jobNumber);
            builder.append("  用户姓名 ：");
            builder.append(jsonUserInfo.get("name").toString());
            builder.append("  所在部门 ：");
            builder.append(jsonUserInfo.get("department").toString());
            return RetResponse.makeOKRsp(builder.toString());
        }*/
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if(employee == null){
            return RetResponse.makeErrRsp("用户不存在，请核对工号后再次查询");
        }else {
           /* Map<String, String> map = new HashMap<>();
            map.put("用户工号",jobNumber);
            map.put("用户姓名",employee.getUserName());
            map.put("所在部门",employee.getDepartment());
            Gson gson = new Gson();
            String json = gson.toJson(map);*/

           StringBuilder builder = new StringBuilder();
            builder.append("用户工号 ：");
            builder.append(jobNumber);
            builder.append("  用户姓名 ：");
            builder.append(employee.getUserName());
            builder.append("  所在部门 ：");
            builder.append(employee.getDepartment());
            /*Gson gson = new Gson();
            String json = gson.toJson(builder.toString());*/
            return RetResponse.makeOKRsp(builder.toString());
        }
    }
}