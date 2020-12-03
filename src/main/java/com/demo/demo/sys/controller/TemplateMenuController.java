package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.TemplateMenu;
import com.demo.demo.sys.entity.treeNode.RootNode;
import com.demo.demo.sys.service.impl.TemplateMenuServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-01
 */
@Controller
@RequestMapping("/templateMenu")
@Slf4j
public class TemplateMenuController {

    @Autowired
    TemplateMenuServiceImpl templateMenuService;

    /**
     *  获取模板目录
     *
     * @param templateId 模板ID
     * @return  模板目录结构数据
     */
    @GetMapping("/getTemplateMenu")
    @ResponseBody
    public RetResult<List<RootNode>>  iGetTemplateMenuListByTemplateId(int templateId){
        return RetResponse.makeOKRsp(templateMenuService.iSelectTemplateMenuListByTemplateId(templateId,0));
    }

    /**
     * 重命名
     *
     * @param text 节点新名字
     * @param id 节点ID
     * @return 重命名失败  true: 重命名成功
     */
    @GetMapping("/changeName")
    @ResponseBody
    public RetResult<Boolean>  changeName(String text,Integer id){
        TemplateMenu templateMenu = templateMenuService.getById(id);
        templateMenu.setText(text);
        templateMenu.setAlive(0);
        return RetResponse.makeOKRsp(templateMenuService.saveOrUpdate(templateMenu));
    }

    /**
     * 添加同级节点
     *
     * @param text 同级节点名字
     * @param id 参照节点的ID, 根据ID找到parent_id,作为同级节点的parent_id
     * @param templateId 节点所在模板ID
     * @return 添加失败  true: 添加成功
     */
    @GetMapping("/addSameNode")
    @ResponseBody
    public RetResult<Boolean>  addSameNode(String text,Integer id,Integer templateId){
        log.info("进入---------------------------------------------");
        RetResult retResult = new RetResult();
        TemplateMenu templateMenu = templateMenuService.getById(id);
        TemplateMenu templateMenuNew = new TemplateMenu();
        templateMenuNew.setText(text);
        templateMenuNew.setParentId(templateMenu.getParentId());
        templateMenuNew.setTemplateId(templateId);
        templateMenuNew.setCheckList("[Y]");
        // 根据工号获取 用户信息
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if(employee!=null){
            templateMenuNew.setFileUpJobNumbers("("+jobNumber+")"+employee.getUserName());
        }else {
            templateMenuNew.setFileUpJobNumbers("0");
        }
        templateMenuNew.setAttendJobNumbers("0");
        templateMenuNew.setFormId(0);
        templateMenuNew.setAlive(0);
        templateMenuNew.setDepIds("0");
        return RetResponse.makeOKRsp(templateMenuService.save(templateMenuNew));
    }

    /**
     * 添加子级节点
     *
     * @param text 子级节点名称
     * @param id 参照节点的ID, 将参照节点的ID作为子级节点的parent_id
     * @param templateId
     * @return false : 添加失败  true: 添加成功
     */
    @GetMapping("/addNextNode")
    @ResponseBody
    public RetResult<Boolean>  addNextNode(String text,Integer id,Integer templateId){
        TemplateMenu templateMenu = templateMenuService.getById(id);
        TemplateMenu templateMenuNew = new TemplateMenu();
        templateMenuNew.setText(text);
        templateMenuNew.setParentId(id);
        templateMenuNew.setAlive(0);
        templateMenuNew.setTemplateId(templateId);
        templateMenuNew.setCheckList("[Y]");
        // 根据工号获取 用户信息
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
        if(employee!=null){
            templateMenuNew.setFileUpJobNumbers("("+jobNumber+")"+employee.getUserName());
        }else {
            templateMenuNew.setFileUpJobNumbers("0");
        }
        templateMenuNew.setAttendJobNumbers("0");
        templateMenuNew.setFormId(0);
        templateMenuNew.setDepIds("0");
        return RetResponse.makeOKRsp(templateMenuService.save(templateMenuNew));
    }

    /**
     * 删除一个节点
     *
     * @param id 删除节点ID
     * @return false : 删除失败  true: 删除成功
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean>  del(Integer id){
        return RetResponse.makeOKRsp(templateMenuService.removeById(id));
    }

    /**
     * 根据ID 获取节点信息
     *
     * @param id 节点ID
     * @return 节点的全部信息
     */
    @GetMapping("/getOne")
    @ResponseBody
    public RetResult<TemplateMenu>  getOne(Integer id){
        return RetResponse.makeOKRsp(templateMenuService.getById(id));
    }

    /**
     * 编辑目录节点
     *
     * @param id 节点ID
     * @param fileUpJobNumbers 文件上传人列表： 默认只有会议创建者
     * @param formId 是否需要表头 0： 不需要表头  1： 根据表头ID
     * @param checkList 是否需要审批 0： 无需审批 1： 审批列表
     * @return false : 编辑失败  true: 编辑成功
     */
    @PostMapping("/editMenu")
    @ResponseBody
    public RetResult<Boolean>  editMenu(Integer id, String fileUpJobNumbers, Integer formId,String checkList,String depIds){
        TemplateMenu templateMenu = templateMenuService.getById(id);
        templateMenu.setFormId(formId);
        templateMenu.setFileUpJobNumbers(fileUpJobNumbers);
        templateMenu.setCheckList(checkList);
        templateMenu.setDepIds(depIds);
        return RetResponse.makeOKRsp(templateMenuService.saveOrUpdate(templateMenu));
    }

}
