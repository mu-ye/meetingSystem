package com.demo.demo.sys.result;

import lombok.Data;

/**
 * @author 牟欢
 * @Classname RetFiles
 * @Description TODO
 * @Date 2020-07-17 14:22
 */
@Data
public class RetFiles {
    /**
     *  返回的文件ID
     */
    private Integer id;
    /**
     *  文件所在路径
     */
    private String text;
    /**
     *  会议上传人姓名
     */
    private String uploadUserName;
    /**
     * 会议上传人 工号
     */
    private String uploadJobNumber;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     *  文件状态 0： 未删除  1： 已删除
     */
    private Integer alive;

}
