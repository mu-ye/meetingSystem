package com.demo.demo.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
@Component
public class Signature {
    private String signature;
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * 自定义生成签名
     * @param params
     * @param appId
     * @param appSecret
     * @return   返回生成的签名
     */
    public String getSignature(Map<String, String> params, String appId, String appSecret) throws UnsupportedEncodingException {
        log.info("appId  ：   "+appId);
        log.info("appSecret  ：  "+appSecret);
        List<String> keys=new ArrayList();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            keys.add(entry.getKey());
        }
        // 将所有参数按key的升序排序
        Collections.sort(keys, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 构造签名的源字符串
        StringBuilder contents=new StringBuilder(appId);
        for (String key : keys) {
            //意思是什么
            if (key==appId||key==appSecret){
                continue;
            }
            contents.append(key).append(params.get(key));
            log.info("key : "+key +"   value: "+params.get(key));
        }
        contents.append(appSecret);

        log.info("拼接好的字符串 ： "+contents);
        //System.out.println(appSecret);
        //System.out.println(contents.toString());

        // 进行hmac sha1 签名
        byte[] bytes= HmacUtils.hmacSha1(appSecret.getBytes("utf-8"),contents.toString().getBytes("utf-8"));
        log.info(bytes.toString());
        //字符串经过Base64编码
        String sign= Base64.encodeBase64String(bytes);
        log.info("URL编码前的签名 ："+sign);
        sign=URLEncoder.encode(sign);
        log.info("URL编码后的签名 ："+sign);
        return sign;
    }
}

