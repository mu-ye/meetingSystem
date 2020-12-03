package com.demo.demo.core.unit;

import java.util.Random;

/**
 * @author 牟欢
 * @Classname RandomString
 * @Description TODO
 * @Date 2020-05-25 16:33
 */
public class RandomString {
    // 使用的是  getRandomString 方法。

    /**
     * version 2 生成随机字母
     * @param number 生成随机字母的位数
     * @return 如果number > 0 正确返回 number位随机字母 ; 如果 number <= 0 ,返回 null
     */
    public static String getRandomString(int number){
        if(number < 1){
            return null;
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            Random random = new Random();
            for(int i = 0 ; i < number ; i++){
                stringBuilder.append(random.nextInt(10));
            }
            return stringBuilder.toString();
        }
    }


    /**
     * 生成N位随机谁字母
     * Version 1
     *
     * @param number
     * @return
     */
    public static String getRandomString1(int number){
        if(number < 1){
            return null;
        }else {
            StringBuilder stringBuilder = new StringBuilder();
            String src = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random random = new Random();
            char[] c = src.toCharArray();
            for(int i = 0 ; i < number ; i++){
                stringBuilder.append(c[random.nextInt(c.length)]);
            }
            return stringBuilder.toString();
        }
    }

}
