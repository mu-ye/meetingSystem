package com.demo.demo.sys.entity.requestbody;

import lombok.Data;

import java.util.List;

/**
 * @author 牟欢
 * @Classname DingDingEntity
 * @Description TODO
 * @Date 2020-07-21 14:51
 */
@Data
public class DingDingEntity {
    public List<String> jobNumberList;
    public String text;
}
