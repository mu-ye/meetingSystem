package com.demo.demo.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.unit.RandomString;
import com.demo.demo.sys.entity.Form;
import com.demo.demo.sys.entity.Title;
import com.demo.demo.sys.service.impl.FormServiceImpl;
import com.demo.demo.sys.service.impl.FormTitleServiceImpl;
import com.demo.demo.sys.service.impl.MeetingMenuServiceImpl;
import com.demo.demo.sys.service.impl.TitleServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
@RequestMapping("/form")
@Slf4j
public class FormController {
    @Autowired
    FormServiceImpl formService;
    @Autowired
    FormTitleServiceImpl formTitleService;
    @Autowired
    TitleServiceImpl titleService;
    @Autowired
    MeetingMenuServiceImpl meetingMenuService;

    @GetMapping("/getText")
    @ResponseBody
    public RetResult<List<Title>> getText(Integer id){
        log.info("q");
        List<Title> titles = titleService.iGetTextByFormId(id);
        for(Title title : titles){
            // 生成控件唯一ID  当前时间戳+10位随机数字
            title.setDataType(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")))+ RandomString.getRandomString(10));
        }
        return RetResponse.makeOKRsp(titles);
    }

    @GetMapping("/getTextByMenuId")
    @ResponseBody
    public RetResult<List<Title>> getTextByMenuId(Integer id){
        log.info("q");
        // 根据menuId 获取 formId :  formId=0,没有表头，返回null; 否则： 返回表头的list数组
        Integer formId = meetingMenuService.getById(id).getFormId();
        log.info(formId.toString());
        if(formId == 0){
            return RetResponse.makeOKRsp(null);
        }else {
            List<Title> titles = titleService.iGetTextByFormId(formId);
            for(Title title : titles){
                // 生成控件唯一ID  当前时间戳+10位随机数字
                title.setDataType(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")))+ RandomString.getRandomString(10));
            }
            return RetResponse.makeOKRsp(titles);
        }
    }

    /**
     * 获取表单列表
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<Form>> getList(){
        log.info("q");
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        QueryWrapper<Form> formQueryWrapper = new QueryWrapper<>();
        formQueryWrapper.eq("alive",0)
                .eq("job_number",jobNumber);
        return RetResponse.makeOKRsp(formService.list(formQueryWrapper));
    }


    /**
     * 添加表单
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public RetResult<Boolean> add(Form form){
        log.info("q");
        String jobNumber= SecurityContextHolder.getContext().getAuthentication().getName();
        form.setJobNumber(jobNumber);
        form.setAlive(0);
        return RetResponse.makeOKRsp(formService.save(form));
    }

    /**
     *  批量删除
     * @param ids 要删除的id数组
     * @return
     */
    @PostMapping("/del")
    @ResponseBody
    public RetResult<Boolean> del(@RequestBody List<Integer> ids){
        log.info("q");
        Boolean flag = false;
        for(Integer id :ids){
            Form form = formService.getById(id);
            form.setAlive(1);
            flag = formService.saveOrUpdate(form);
        }
        return RetResponse.makeOKRsp(flag);
    }

    @PostMapping("/rename")
    @ResponseBody
    RetResult<String> rename(Integer id,String name){
        log.info("q");
        Form form = formService.getById(id);
        form.setName(name);
        Boolean flag = formService.saveOrUpdate(form);
        if(flag){
            return RetResponse.makeOKRsp(name);
        }else {
            return RetResponse.makeErrRsp("内部错误，及时联系管理员");
        }

    }












}

