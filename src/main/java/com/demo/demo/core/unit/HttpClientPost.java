package com.demo.demo.core.unit;
import com.demo.demo.core.common.SystemCommon;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 牟欢
 * @Classname HttpClientPost
 * @Description TODO
 * @Date 2020-04-15 10:38
 */
@Slf4j
@Component
public class HttpClientPost {
    public String getUrlPost(String localPath,String downloadUrl,String waterMark,String fileName) throws IOException {
        String result ="内部错误";
        String httpEntity=getFileInfo(localPath,downloadUrl,waterMark,fileName);
        log.info("httpEntity: "+httpEntity);
        //创建创建HttpClient对象对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // URL
        StringBuffer params = new StringBuffer();
        try {
            // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
            params.append("appid=" + URLEncoder.encode(SystemCommon.APP_ID, "utf-8"));
            params.append("&");
            params.append("signature="+SystemCommon.APP_SECRET);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // 内网地址
        HttpPost httpPost = new HttpPost("http://192.168.138.134:8237/v2/view"+ "?" + params);
        // 修改后的外网地址
       // HttpPost httpPost = new HttpPost("http://112.4.141.91:2021/v2/view"+ "?" + params);
        // 响应模型
        CloseableHttpResponse response = null;
        //响应头
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        StringEntity entity = new StringEntity(httpEntity, "UTF-8");
        log.info("body:"+entity.toString());
        httpPost.setEntity(entity);
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseEntity = new BufferedHttpEntity(responseEntity); // 可重复使用的 responseEntity
                result= EntityUtils.toString(responseEntity); //获取返回结果
                log.info("响应状态为:" + response.getStatusLine());
                log.info("响应内容长度为:" + responseEntity.getContentLength());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       log.info("最终响应结果： "+result);
        return result;
    }

    public String getFileInfo(String localPath, String downloadUrl,String waterMark,String fileName) throws IOException {
        Map<String, Object> textMap = new HashMap<>();
        File file = new File(localPath);
        String uniqueId=null;
        long size=0;
        if(file.exists()){
            uniqueId=DigestUtils.sha1Hex(new FileInputStream(localPath));
            size= file.length(); // 获取文件大小
        }
        textMap.put("enableCopy",true);
        textMap.put("watermarkType","1");
        textMap.put("watermark",waterMark);
        textMap.put("watermarkSetting","{\t\"width\": 195,\n" +
                "\t\t\t\"height\": 170}");
        textMap.put("enablePrint",true);
        textMap.put("url",downloadUrl);
        textMap.put("getFileWay","download");
        textMap.put("uniqueId",uniqueId);
        textMap.put("size",size);
        textMap.put("fname",fileName);
        JSONObject jsonObject = JSONObject.fromObject(textMap);
        log.info("文件信息："+jsonObject.toString());
        Map<String,JSONObject> resultMap = new HashMap<>();
        resultMap.put("fileInfo3rd",jsonObject);
        JSONObject result = JSONObject.fromObject(resultMap);
        log.info("post 请求方式最终返回josn 信息："+result.toString());
        return result.toString();
    }
}
