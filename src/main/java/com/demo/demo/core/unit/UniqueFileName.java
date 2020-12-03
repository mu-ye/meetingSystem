package com.demo.demo.core.unit;

/**
 * @author 牟欢
 * @Classname UniqueFileName
 * @Description TODO
 * @Date 2020-05-26 10:26
 */
public class UniqueFileName {
    /**
     * 根据 文件名生成唯一的文件名（）
     * @param fileName
     * @return 唯一文件名  文件名+当前时间戳+六位随机数字
     */
    public static String getUniqueFileName(String fileName){
        String fileNameSuffix=fileName.substring(fileName.lastIndexOf("."));
        String fileNameMain=fileName.substring(0,fileName.lastIndexOf("."));
        return fileNameMain+String.valueOf(System.currentTimeMillis())+String.valueOf((int)((Math.random()*9+1)*100000))+fileNameSuffix;
    }
}
