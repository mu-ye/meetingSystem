package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.UserInfoUtil;
import com.demo.demo.sys.domain.Employee;
import com.demo.demo.sys.entity.Template;
import com.demo.demo.sys.entity.TemplateMenu;
import com.demo.demo.sys.service.impl.TemplateMenuServiceImpl;
import com.demo.demo.sys.service.impl.TemplateServiceImpl;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-01
 */
@Controller
@RequestMapping("/template")
@Slf4j
public class TemplateController {
    @Autowired
    TemplateServiceImpl templateService;
    @Autowired
    TemplateMenuServiceImpl templateMenuService;

    /**
     * 新建模板
     *
     * @param name
     * @return List[0] 模板ID ; List[1]  模板目录ID
     */
    @ResponseBody
    @PostMapping("/add")
    public RetResult<String> addTemplate(String name) {
        String result = null;
        if (name.equals(null)) {
            return RetResponse.makeErrRsp("模板名称不能为空");
        } else {
            QueryWrapper<Template> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", name);
            if (!templateService.list(queryWrapper).isEmpty()) {
                return RetResponse.makeErrRsp("模板名称已经被占用，请重新输入模板名 ");
            } else {
                String jobNumber = SecurityContextHolder.getContext().getAuthentication().getName();
                // 根据工号获取用户信息
                Employee employee = UserInfoUtil.getEmployeeByIdFromServer(jobNumber);
                Template template = new Template();
                template.setName(name);
                template.setAlive(0);
                template.setJobNumber(jobNumber);
                templateService.save(template);
                log.info("添加模板成功");
                Integer templateId = templateService.getOne(queryWrapper).getId();
                log.info("模板创建成功，id" + templateId);
                // 新建一个template_menu
                TemplateMenu templateMenu = new TemplateMenu();
                // 默认值为0
                templateMenu.setText("根节点");
                // 默认父节点为0
                templateMenu.setParentId(0);
                templateMenu.setTemplateId(templateId);
                templateMenu.setCheckList("[Y]");
                // 默认创建人可以上传文件
                templateMenu.setFileUpJobNumbers("(" + jobNumber + ")" + employee.getUserName());
                templateMenu.setAttendJobNumbers("0");
                templateMenu.setFormId(0);
                templateMenu.setAlive(0);
                templateMenu.setDepIds("0");
                if (employee != null) {
                    templateMenu.setFileUpJobNumbers("(" + jobNumber + ")" + employee.getUserName());
                } else {
                    templateMenu.setFileUpJobNumbers("0");
                }
                templateMenuService.save(templateMenu);
                log.info("添加模板目录成功");

                QueryWrapper<TemplateMenu> templateMenuQueryWrapper = new QueryWrapper<>();
                templateMenuQueryWrapper.eq("template_id", templateId)
                        .eq("parent_id", 0);
                TemplateMenu templateMenu1 = templateMenuService.getOne(templateMenuQueryWrapper);
                log.info("templateMenu1"+templateMenu1.toString());
                Map<String, Integer> map = new HashMap<>();
                // 新建模板Id
                map.put("templateId",templateId);
                // 模板目录 根节点Id
                map.put("menuId",templateMenu1.getId());
                Gson gson = new Gson();
                String mapJson = gson.toJson(map);
                log.info("返回前端结果："+mapJson);
                return RetResponse.makeOKRsp(mapJson);
            }
        }
    }

    /**
     *  根据用户工号列出用户所有模板
     *
     *  @return 模板列表
     */
    @ResponseBody
    @GetMapping("/getList")
    public RetResult<List<Template>> getList() {
        String jobNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.eq("job_number", jobNumber);
        List<Template> templateList = templateService.list(templateQueryWrapper);
        return RetResponse.makeOKRsp(templateList);
    }

    /**
     * 批量删除
     *
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(@RequestBody List<Integer> ids){
        for(Integer id :ids){
            // 删除模板
            templateService.removeById(id);
            // 删除模板 目录结构
            QueryWrapper<TemplateMenu> templateMenuQueryWrapper = new QueryWrapper<>();
            templateMenuQueryWrapper.eq("template_id",id);
            List<TemplateMenu> templateMenuList = templateMenuService.list(templateMenuQueryWrapper);
            if(!templateMenuList.isEmpty()){
                for(TemplateMenu templateMenu :templateMenuList){
                    templateMenuService.removeById(templateMenu.getId());
                }
            }
        }
        return RetResponse.makeOKRsp(true);
    }

    /**
     *  模板重命名
     * @param id 模板ID
     * @param name 模板名称
     * @return
     */
    @PostMapping("/rename")
    @ResponseBody
    public RetResult<Boolean> rename(Integer id,String name){
        Template template = templateService.getById(id);
        template.setName(name);
        return RetResponse.makeOKRsp(templateService.saveOrUpdate(template));
    }


}

