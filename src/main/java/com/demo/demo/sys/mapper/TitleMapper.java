package com.demo.demo.sys.mapper;

import com.demo.demo.core.ret.RetTitle;
import com.demo.demo.sys.entity.Title;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-16
 */
public interface TitleMapper extends BaseMapper<Title> {
    /**
     * 根据表格formId获取 表格内容的json 数组
     *
     * @param formId 表格Id
     * @return
     */
    public List<Title> iSelectTextByFormId(@Param("formId") Integer formId);

    /**
     *  获取编辑 表单的表头列表
     *
     * @param formId 表格Id
     * @return
     */
    public List<RetTitle> iSelectFormTitlesByFormIdGroupByAutoIndex(@Param("formId") Integer formId);
}
