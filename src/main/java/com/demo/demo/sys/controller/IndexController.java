package com.demo.demo.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.*;
import com.demo.demo.sys.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author 牟欢
 * @Classname IndexController
 * @Description TODO
 * @Date 2020-05-26 15:22
 */
@Controller
@RequestMapping("/index")
@Slf4j
public class IndexController {
    @Autowired
    UserInfoServiceImpl userInfoService;
    @Autowired
    TemplateServiceImpl templateService;
    @Autowired
    FormServiceImpl formService;
    @Autowired
    TitleServiceImpl titleService;
    @Autowired
    UserGroupServiceImpl userGroupService;
    @Autowired
    DepartmentServiceImpl departmentService;

    /**
     *  跳转到发送钉钉消息界面
     */
    @RequestMapping("/sendMessage")
    public String sendMessage(Model model){
        List<UserGroup> userGroups = userGroupService.list();
        model.addAttribute("groupList",userGroups);
        return "SendMessage";
    }



    /**
     * 转到 登陆页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * 转到 主页页面
     * @return
     */
    @RequestMapping("/main")
    public String Main(Model model){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if(employee != null){
            model.addAttribute("userName",employee.getUserName());
        }else {
            model.addAttribute("userName","用户不存在");
        }
        return "main";
    }

    /**
     * 转到 新建会议页面
     *
     * @return
     */
    @RequestMapping("/manageMeeting")
    public String manageMeeting(Model model){
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.eq("job_number",jobNumber);
        List<Template> templateList = templateService.list(templateQueryWrapper);
        model.addAttribute("templateList",templateList);
        return "ManageMeeting";
    }

    /**
     * 转到 部门管理 页面
     * @return
     */
    @RequestMapping("/manageDepartment")
    public String ManageDepartment(){
        return "ManageDepartment";
    }

    /**
     * 转到 角色管理 页面
     * @return
     */
    @RequestMapping("/manageRole")
    public String ManageRole(){
        return "ManageRole";
    }

    /**
     * 转到 角色管理 页面
     * @return
     */
    @RequestMapping("/editTitle")
    public String EditTitle(Model model){
        List<Title> titleList = titleService.list();
        model.addAttribute("titleList",titleList);
        return "EditTitle";
    }



    /**
     * 转到 模板管理 页面
     * @return
     */
    @RequestMapping("/editGroup")
    public String editGroup(){
        return "EditGroup";
    }


    /**
     * 转到编辑群组页面
     * @return
     */
    @RequestMapping("/manageTemplate")
    public String ManageTemplate(){
        return "ManageTemplate";
    }

    /**
     * 转到 新建模板 页面
     * @return
     */
    @RequestMapping("/newTemplate")
    public String NewTemplate(Model model){
        // 添加全部部门
        QueryWrapper<Form> formQueryWrapper = new QueryWrapper<>();
        formQueryWrapper.eq("alive",0);
        List<Form> formList = formService.list(formQueryWrapper);
        model.addAttribute("formList",formList);
        // 添加全部部门
        List<Department> departmentList = departmentService.list();
        model.addAttribute("departmentList",departmentList);
        // 添加全部群组
        QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
        userGroupQueryWrapper.eq("alive",0);
        List<UserGroup> userGroups = userGroupService.list(userGroupQueryWrapper);
        model.addAttribute("userGroups",userGroups);

        return "NewTemplate";
    }

    /**
     * 转到 群组管理 页面
     * @return
     */
    @RequestMapping("/manageGroup")
    public String ManageGroup(){
        return "ManageGroup";
    }

    /**
     * 转到 免登IP管理 页面
     * @return
     */
    @RequestMapping("/manageIp")
    public String ManageIp(){
        return "ManageIp";
    }

    /**
     * 转到 历史会议 页面
     * @return
     */
    @RequestMapping("/historyMeeting")
    public String HistoryMeeting(){
        return "MeetingHistory";
    }

    /**
     * 转到 我的待办 页面
     * @return
     */
    @RequestMapping("/toCheck")
    public String ToCheck(){
        return "MeetingUpload";
    }

    /**
     * 转到 我的会议 页面
     * @return
     */
    @RequestMapping("/myMeeting")
    public String MyMeeting(){
        return "MyMeeting";
    }

    /**
     * 转到 我的表单 页面
     * @return
     */
    @RequestMapping("/manageForm")
    public String ManageForm(){
        return "ManageForm";
    }

    /**
     * 转到 我的表头 页面
     * @return
     */
    @RequestMapping("/manageTitle")
    public String manageTitle(){
        return "ManageTitle";
    }

    /**
     * 转到 我的会议 页面
     * @return
     */
    @RequestMapping("/manageUser")
    public String ManageUser(){
        return "ManageUser";
    }


    @RequestMapping("/editTemplate")
    public String editTemplate(Model model){
        QueryWrapper<Form> formQueryWrapper = new QueryWrapper<>();
        formQueryWrapper.eq("alive",0);
        List<Form> formList = formService.list(formQueryWrapper);
        model.addAttribute("formList",formList);
        // 添加全部部门
        List<Department> departmentList = departmentService.list();
        model.addAttribute("departmentList",departmentList);
        // 添加全部群组
        QueryWrapper<UserGroup> userGroupQueryWrapper = new QueryWrapper<>();
        userGroupQueryWrapper.eq("alive",0);
        List<UserGroup> userGroups = userGroupService.list(userGroupQueryWrapper);
        model.addAttribute("userGroups",userGroups);
        return "EditTemplate";
    }


    @RequestMapping("/editMeeting")
    public String editMeeting(Model model){
        // 添加模板列表
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.eq("job_number",jobNumber);
        List<Template> templateList = templateService.list(templateQueryWrapper);
        model.addAttribute("templateList",templateList);
        // 添加拥有的表格列表
        QueryWrapper<Form> formQueryWrapper = new QueryWrapper<>();
        formQueryWrapper.eq("alive",0);
        List<Form> formList = formService.list(formQueryWrapper);
        model.addAttribute("formList",formList);
        // 添加用户拥有的全部群组
        List<UserGroup> userGroups = userGroupService.list();
        model.addAttribute("groupList",userGroups);

        // 添加全部部门
        List<Department> departmentList = departmentService.list();
        model.addAttribute("departmentList",departmentList);
        return "EditMeeting";
    }

    /**
     * 转到文件上传者界面
     * @return
     */
    @RequestMapping("/uploadMeeting")
    public String uploadMeeting(Model model){
        // 添加全部部门
        List<Department> departmentList = departmentService.list();
        model.addAttribute("departmentList",departmentList);
        return "UploadFile";
    }

    /**
     * 转到文件上传者界面
     * @return
     */
    @RequestMapping("/todo")
    public String todo(){
        return "ToDo";
    }

    /**
     * 跳转到领导管理界面
     * @return
     */
    @RequestMapping("/manageLeader")
    public String leaderManage(Model model){
        // 添加全部部门
        List<Department> departmentList = departmentService.list();
        model.addAttribute("departmentList",departmentList);
        return "ManageLeader";
    }

    /**
     * 转到文件上传者界面
     * @return
     */
    @RequestMapping("/editFiles")
    public String editFiles(Model model){
        // 添加全部部门
        List<Department> departmentList = departmentService.list();
        model.addAttribute("departmentList",departmentList);
        return "EditFiles";
    }

    @RequestMapping("/downLoadFiles")
    public String downLoadFiles(){
        return "DownLoadFile";
    }


}
