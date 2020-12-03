package com.demo.demo.sys.service.impl;

import com.demo.demo.core.ret.RetTitle;
import com.demo.demo.sys.entity.Title;
import com.demo.demo.sys.mapper.TitleMapper;
import com.demo.demo.sys.service.ITitleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-16
 */
@Service
public class TitleServiceImpl extends ServiceImpl<TitleMapper, Title> implements ITitleService {
    @Autowired
    TitleMapper titleMapper;

    @Override
    public List<Title> iGetTextByFormId(Integer formId) {
        return titleMapper.iSelectTextByFormId(formId);
    }

    @Override
    public List<RetTitle> iGetFormTitlesByFormIdGroupByAutoIndex(Integer formId) {
        return titleMapper.iSelectFormTitlesByFormIdGroupByAutoIndex(formId);
    }

}
