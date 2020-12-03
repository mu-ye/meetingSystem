package com.demo.demo.sys.result;

import lombok.Data;

/**
 * @author 牟欢
 * @Classname RetUserInfo
 * @Description TODO
 * @Date 2020-05-07 9:19
 */
@Data
public class RetUserInfo {

    String userName;
    /**
     *  返回所在表格的Id
     */
    Integer id;
    String jobNumber;
    String departmentName;
}
