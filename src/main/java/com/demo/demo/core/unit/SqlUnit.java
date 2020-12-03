/*
package com.demo.demo.core.unit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.sys.entity.Department;
import com.demo.demo.sys.entity.OtherMenu;
import com.demo.demo.sys.entity.RecordMenu;
import com.demo.demo.sys.entity.UserRole;
import com.demo.demo.sys.service.impl.DepartmentServiceImpl;
import com.demo.demo.sys.service.impl.OtherMenuServiceImpl;
import com.demo.demo.sys.service.impl.RecordFileServiceImpl;
import com.demo.demo.sys.service.impl.RecordMenuServiceImpl;
import org.apache.velocity.runtime.directive.contrib.For;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

*/
/**
 * @author 牟欢
 * @Classname SqlUnit
 * @Description TODO
 * @Date 2020-04-29 13:40
 *//*

@Component
public class SqlUnit {
    @Autowired
    DepartmentServiceImpl departmentService;
    @Autowired
    OtherMenuServiceImpl otherMenuService;
    @Autowired
    RecordMenuServiceImpl recordMenuService;

    public void setOtherMenu(Integer parentID){
        List<Department> departmentList =departmentService.list();
        for(Department department : departmentList){
            OtherMenu otherMenu = new OtherMenu();
            otherMenu.setAlive(1);
            otherMenu.setParentId(parentID);
            otherMenu.setMenuName(department.getDepartmentName());
            otherMenu.setDepId(department.getId());
            otherMenuService.save(otherMenu);
        }
    }

    public void setRecordMenu(){
        RecordMenu otherMenu1 = new RecordMenu();
        otherMenu1.setAlive(1);
        otherMenu1.setParentId(0);
        otherMenu1.setMenuName("记录系统");
        recordMenuService.save(otherMenu1);
        List<Department> departmentList =departmentService.list();
        for(Department department : departmentList){
            RecordMenu otherMenu = new RecordMenu();
            otherMenu.setAlive(1);
            otherMenu.setParentId(1);
            otherMenu.setMenuName(department.getDepartmentName());
            otherMenu.setDepId(department.getId());
            recordMenuService.save(otherMenu);
        }
    }

}
*/
