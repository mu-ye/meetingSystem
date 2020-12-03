package com.demo.demo.sys.result;

import lombok.Data;

/**
 * @author 牟欢
 * @Classname RetDownloadFile
 * @Description TODO
 * @Date 2020-07-28 14:00
 */
@Data
public class RetDownloadFile {
    /**
     *  返回的文件ID
     */
    private Integer id;
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
     *  文件URL
     */
    private String url;


}
