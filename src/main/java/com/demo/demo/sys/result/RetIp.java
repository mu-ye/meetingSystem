package com.demo.demo.sys.result;

import lombok.Data;
import springfox.documentation.service.ApiListing;

/**
 * @author 牟欢
 * @Classname RetIp
 * @Description TODO
 * @Date 2020-05-27 15:44
 */
@Data
public class RetIp {
    private Integer id;
    private String ip;
    private String jobNumber;
    private String departmentName;
    private String userName;
}
