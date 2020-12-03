package com.demo.demo.core.unit;

import org.springframework.stereotype.Component;

@Component
public class MyFileTransformation {
    /**
     * 将文件名中添加当前系统时间确保文件名唯一
     *
     * @param fileName
     * @return String
     */
    public static String fileNameTransformationAddDateFileName(String fileName) {
        String fileNameSuffix = fileName.substring(fileName.lastIndexOf("."));
        String fileNameMain = fileName.substring(0, fileName.lastIndexOf("."));
        return fileNameMain + String.valueOf(System.currentTimeMillis())+ fileNameSuffix;
    }
}
