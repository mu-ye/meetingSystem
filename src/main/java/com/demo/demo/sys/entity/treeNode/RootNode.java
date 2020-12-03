package com.demo.demo.sys.entity.treeNode;

import lombok.Data;

import java.util.List;

/**
 * @author 牟欢
 * @Classname RootNode
 * @Description TODO
 * @Date 2020-04-29 16:32
 */
@Data
public class RootNode {
    private  Integer id;
    private  String text;
    private  NodeState state;
    private  List<RootNode> nodes;
    /**
     *  自定义树形  是否为根节点
     */
    private  Boolean isLeaf;
    /**
     *  自定义树形 是否可以文件上传  false = 不可以上传文件； true : 可以上传文件
     */
    private  Boolean isShow;

}
