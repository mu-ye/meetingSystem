package com.demo.demo.sys.service;

import com.demo.demo.core.ret.RetTitle;
import com.demo.demo.sys.entity.Title;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-16
 */
public interface ITitleService extends IService<Title> {
    /**
     * 根据表格Id获取 表格内容的json 数组
     *
     * @param formId 表格Id
     * @return
     */
    public List<Title> iGetTextByFormId(@Param("formId") Integer formId);

    /**
     * 根据表格Id获取 表格内容的json 数组
     *
     * @param formId 表格Id
     * @return
     */
    public List<RetTitle> iGetFormTitlesByFormIdGroupByAutoIndex(@Param("formId") Integer formId);
}


