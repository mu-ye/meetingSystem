package com.demo.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.sys.entity.TemplateMenu;
import com.demo.demo.sys.entity.treeNode.RootNode;
import com.demo.demo.sys.mapper.TemplateMenuMapper;
import com.demo.demo.sys.service.ITemplateMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 牟欢
 * @since 2020-06-01
 */
@Service
@Slf4j
public class TemplateMenuServiceImpl extends ServiceImpl<TemplateMenuMapper, TemplateMenu> implements ITemplateMenuService {

    @Autowired
    TemplateMenuMapper templateMenuMapper;

    /**
     * 根据模板ID 获取模板目录结构
     *
     * @param templateId
     * @return 目录结构list
     */
    @Override
    public List<RootNode> iSelectTemplateMenuListByTemplateId(int templateId, int parentId){
        List<RootNode> resultRootNodeList = new ArrayList<>();
        QueryWrapper<TemplateMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id",templateId)
                .eq("parent_id",parentId);
        List<TemplateMenu> templateMenuList = templateMenuMapper.selectList(queryWrapper);
        if(templateMenuList.isEmpty()){
            return null;
        }else{
            for(TemplateMenu templateMenu : templateMenuList){
                List<RootNode> rootNodeList = iSelectTemplateMenuListByTemplateId(templateId,templateMenu.getId());
                RootNode rootNode = new RootNode();
                if(rootNodeList != null) {

                    rootNode.setId(templateMenu.getId());
                    rootNode.setText(templateMenu.getText());
                    rootNode.setIsLeaf(false);
                    rootNode.setNodes(rootNodeList);
                }else {
                    rootNode.setId(templateMenu.getId());
                    rootNode.setText(templateMenu.getText());
                    rootNode.setIsLeaf(true);
                    rootNode.setNodes(null);
                }
                resultRootNodeList.add(rootNode);

            }
            return resultRootNodeList;
        }
    }
}
