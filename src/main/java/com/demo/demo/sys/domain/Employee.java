package com.demo.demo.sys.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 职工
 *
 * @author RCNJTECH
 * @date 2020/4/15 11:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    /**
     *  用户工号
     */
    private String jobNumber;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户所在部门
     */
    private String department;

}
