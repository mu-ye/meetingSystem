package com.demo.demo.sys.controller;


import com.demo.demo.core.ret.RetResponse;
import com.demo.demo.core.ret.RetResult;
import com.demo.demo.core.ret.RetTitle;
import com.demo.demo.core.unit.StrUtil;
import com.demo.demo.sys.entity.Title;
import com.demo.demo.sys.service.impl.TitleServiceImpl;
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
@Slf4j
@RequestMapping("/title")
public class TitleController {
    @Autowired
    TitleServiceImpl titleService;

    /**
     * 返回免登我的表单列表列表
     * @return
     */
    @GetMapping("/getList")
    @ResponseBody
    public RetResult<List<Title>> getList(){
        return RetResponse.makeOKRsp(titleService.list());
    }

    /**
     * 返回免登我的表单列表列表
     * @return
     */
    @GetMapping("/getEditFormTitleList")
    @ResponseBody
    public RetResult<List<RetTitle>> getEditFormTitleList(Integer formId){
        return RetResponse.makeOKRsp(titleService.iGetFormTitlesByFormIdGroupByAutoIndex(formId));
    }



    @GetMapping("/getTitlesByFormId")
    @ResponseBody
    public RetResult<List<Title>> getTitlesByFormId(Integer formId){
        return RetResponse.makeOKRsp(titleService.list());
    }


    /**
     *  添加表头
     * @param title
     * @return
     */
    @PostMapping("/addTitle")
    @ResponseBody
    public RetResult<Boolean> addInput(Title title){
        log.info("=================================添加表头=======================================");
        log.info(title.getTitleType());
        if("select".equals(title.getTitleType())){
            log.info("select进行转换");
            title.setData(StrUtil.toSelect(title.getData(),"##","@@"));
         }
        title.setAlive(0);
        title.setDataType("-");
        return RetResponse.makeOKRsp(titleService.save(title));
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
            flag = titleService.removeById(id);
        }
        return RetResponse.makeOKRsp(flag);
    }

    @GetMapping("/getOneById")
    @ResponseBody
    public RetResult<Title> getOneById(Integer id){
        Title title = titleService.getById(id);
        if(title == null){
            return RetResponse.makeErrRsp(null);
        }else {
            return RetResponse.makeOKRsp(titleService.getById(id));
        }
    }
    @PostMapping("/edit")
    @ResponseBody
    public RetResult<Boolean> getOneById(Title title){
        title.setAlive(0);
        return RetResponse.makeOKRsp(titleService.saveOrUpdate(title));
    }



}
