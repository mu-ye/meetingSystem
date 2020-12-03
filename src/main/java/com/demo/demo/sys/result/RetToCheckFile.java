package com.demo.demo.sys.result;

import lombok.Data;

/**
 * @author 牟欢
 * @Classname RetToCheckFile
 * @Description TODO
 * @Date 2020-06-10 14:39
 */
@Data
public class RetToCheckFile {
    /**
     *  返回的文件ID
     */
    private Integer id;
    /**
     *  会议名称
     */
    private String name;
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
     * 文件审核状态
     */
    private Integer pass;

    /**
     * 文件被驳回理由
     */
    private String refuseReason;

    /**
     * 文件的审批流程
     */
    private String checkList;


}
