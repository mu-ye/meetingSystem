package com.demo.demo.sys.service;

import com.demo.demo.sys.entity.TemplateMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.demo.sys.entity.treeNode.RootNode;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-01
 */
public interface ITemplateMenuService extends IService<TemplateMenu> {
    /**
     * 获取模板目录结构
     * @param templateId 模板ID
     * @param parentId 模板的开始位置
     * @return
     */
    public List<RootNode> iSelectTemplateMenuListByTemplateId(int templateId,int parentId);
}
