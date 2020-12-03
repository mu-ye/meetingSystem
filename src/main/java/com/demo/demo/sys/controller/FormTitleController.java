package com.demo.demo.sys.controller;


import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.sys.entity.FormTitle;
import com.demo.demo.sys.service.impl.FormTitleServiceImpl;
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
 * @since 2020-06-12
 */
@Controller
@RequestMapping("/formTitle")
@Slf4j
public class FormTitleController {
    @Autowired
    FormTitleServiceImpl formTitleService;

    @PostMapping("/editAutoIndex")
    @ResponseBody
    public RetResult<Boolean> editAutoIndex(Integer formTitleId,Integer autoIndex,Integer width){
        FormTitle formTitle = formTitleService.getById(formTitleId);
        formTitle.setAutoIndex(autoIndex);
        formTitle.setWidth(width);
        return RetResponse.makeOKRsp(formTitleService.saveOrUpdate(formTitle));
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
            FormTitle formTitle = formTitleService.getById(id);
            formTitle.setAlive(1);
            flag = formTitleService.saveOrUpdate(formTitle);
        }
        return RetResponse.makeOKRsp(flag);
    }

    /**
     * 给表单添加 表头
     *
     * @param formId 表单Id
     * @param titleIds 表头列表
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<Boolean> add(String titleIds,Integer formId){
        log.info("formId :" +formId);
        log.info("titleIds :" +titleIds);
        String[] titleIdArr = titleIds.split(",");
        for(int i = 0 ; i< titleIdArr.length ; i++){
            Integer titleId = Integer.parseInt(titleIdArr[i]);
            FormTitle formTitle = new FormTitle();
            formTitle.setFormId(formId);
            formTitle.setTitleId(titleId);
            formTitle.setAutoIndex(1);
            formTitle.setAlive(0);
            // 设置默认宽度
            formTitle.setWidth(5);
            formTitleService.save(formTitle);
        }
        return RetResponse.makeOKRsp();
    }

}
