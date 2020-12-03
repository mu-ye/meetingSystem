package com.demo.demo.sys.controller;


import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.sys.entity.Department;
import com.demo.demo.sys.service.impl.DepartmentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/dep")
@Slf4j
public class DepartmentController {
    @Autowired
    DepartmentServiceImpl departmentService;

    /**
     * 获取部门列表
     * @return
     */
    @GetMapping("/getLists")
    @ResponseBody
    public List<Department> getLists(){
        return departmentService.list();
    }

    /**
     * 添加部门
     * @param departmentName
     * @return
     */
    @PostMapping("/add")
    public String add(@RequestParam("departmentName") String departmentName){
        log.info("添加部门");
        Department department = new Department();
        department.setDepartmentName(departmentName);
        department.setAlive(1);
        departmentService.save(department);
        return "redirect:/index/manageDepartment";
        //return "ManageDepartment";
    }

    /**
     *  批量删除 部门
     * @param ids
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Integer> del(@RequestBody List<Integer> ids){
        for(Integer id :ids){
            departmentService.removeById(id);
        }
        return RetResponse.makeOKRsp(1);
    }

}
