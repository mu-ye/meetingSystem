package com.demo.demo.sys.entity.treeNode;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 牟欢
 * @Classname NodeState
 * @Description TODO
 * @Date 2020-06-09 16:29
 */
@Data
public class NodeState {
    public Boolean checked;
    /**
     * 节点是否可用
     */
    public Boolean disabled;
    /**
     * 节点是否展开
     */
    public Boolean expanded;
     /**
     *  节点是否处于选中状态
     */
    public Boolean selected;

    /**
     *  默认构造函数
     */
    public NodeState(){
        this.checked = false;
        this.disabled = false;
        this.expanded = false;
        this.selected = false;
    }

}
