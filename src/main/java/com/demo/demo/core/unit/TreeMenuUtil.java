package com.demo.demo.core.unit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.demo.sys.entity.TemplateMenu;
import com.demo.demo.sys.entity.treeNode.RootNode;
import com.demo.demo.sys.service.impl.TemplateMenuServiceImpl;
import com.demo.demo.sys.service.impl.TemplateServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 牟欢
 * @Classname TreeMenuUtil
 * @Description TODO
 * @Date 2020-06-02 9:12
 */
@Slf4j
public class TreeMenuUtil {

    @Autowired
    TemplateMenuServiceImpl templateMenuService;

    public List<RootNode> getTemplateMenuListByTemplateId(int templateId){
        List<RootNode> resultRootNodeList = new ArrayList<>();
        QueryWrapper<TemplateMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id",templateId)
                    .eq("parent_id",0);
        List<TemplateMenu> templateMenuList = templateMenuService.list(queryWrapper);
        for(TemplateMenu templateMenu : templateMenuList){
            List<RootNode> rootNodeList = getTemplateMenuListByTemplateId(templateMenu.getId());
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
