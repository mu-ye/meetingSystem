package com.demo.demo.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
@Slf4j
public class httpClientGet {

    /**
     * 传入 文件地址   水印内容  获取文件类型（本地文件  下载文件） 生成的签名
     *
     * @param fileURL
     * @param waterMark
     * @param fileType
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getURL(String fileURL, String waterMark, String fileType) throws UnsupportedEncodingException {
        String result = null;
        //创建httpclient get 方法请求文件预览地址
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringBuffer params = new StringBuffer();
        // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
        params.append("fileURL=" + URLEncoder.encode(fileURL, "UTF-8"));
        params.append("&");
        params.append("waterMark=" + URLEncoder.encode(waterMark, "UTF-8"));
        params.append("&");
        params.append("fileType=" + URLEncoder.encode(fileType, "UTF-8"));
        String url = "http://192.168.138.134:2333/getURL?" + params;
        log.info("请求的连接为：" + url);
        //创建get请求
        HttpGet httpGet = new HttpGet(url);
        //创建响应模型
        CloseableHttpResponse response = null;
        try {
            //配置信息    custom: 自定义
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时的时间（单位毫秒）
                    .setConnectTimeout(5000)
                    //设置请求超时的时间
                    .setConnectionRequestTimeout(5000)
                    //设置socket读写超时的时间
                    .setSocketTimeout(5000)
                    //设置是否允许重订向
                    .setRedirectsEnabled(true).build();
            //将上面的配置信息应用到这get请求里面
            httpGet.setConfig(requestConfig);
            //由客户端执行（发送）get请求
            response = httpClient.execute(httpGet);
            //从响应模块中获得响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info(responseEntity.getContent() + "aa");
            System.out.println("响应状态为： " + response.getStatusLine());
            String aa = null;
            if (responseEntity != null) {
                System.out.println("响应内容的长度为： " + responseEntity.getContentLength());
                result = EntityUtils.toString(responseEntity);
                System.out.println("响应信息的内容为： " + result);
            }
        } catch (
                ClientProtocolException e) {
            //打印 客户端协议异常
            e.printStackTrace();
        } catch (
                ParseException e) {
            //打印 解析异常
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //释放资源
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
        return result;
    }



    public String getURLPost(String localPath,String downloadUrl,String waterMark,String fileName) throws UnsupportedEncodingException {
        String result = null;
        //创建httpclient get 方法请求文件预览地址
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringBuffer params = new StringBuffer();
        // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
        params.append("localPath=" + URLEncoder.encode(localPath, "UTF-8"));
        params.append("&");
        params.append("downloadUrl=" + URLEncoder.encode(downloadUrl, "UTF-8"));
        params.append("&");
        params.append("waterMark=" + URLEncoder.encode(waterMark, "UTF-8"));
        params.append("&");
        params.append("fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        String url = "http://192.168.138.134:2333/getURLPost?" + params;
        log.info("请求的连接为：" + url);
        //创建get请求
        HttpGet httpGet = new HttpGet(url);
        //创建响应模型
        CloseableHttpResponse response = null;
        try {
            //配置信息    custom: 自定义
            RequestConfig requestConfig = RequestConfig.custom()
                    //设置连接超时的时间（单位毫秒）
                    .setConnectTimeout(5000)
                    //设置请求超时的时间
                    .setConnectionRequestTimeout(5000)
                    //设置socket读写超时的时间
                    .setSocketTimeout(5000)
                    //设置是否允许重订向
                    .setRedirectsEnabled(true).build();
            //将上面的配置信息应用到这get请求里面
            httpGet.setConfig(requestConfig);
            //由客户端执行（发送）get请求
            response = httpClient.execute(httpGet);
            //从响应模块中获得响应实体
            HttpEntity responseEntity = response.getEntity();
            log.info(responseEntity.getContent() + "aa");
            System.out.println("响应状态为： " + response.getStatusLine());
            String aa = null;
            if (responseEntity != null) {
                System.out.println("响应内容的长度为： " + responseEntity.getContentLength());
                result = EntityUtils.toString(responseEntity);
                System.out.println("响应信息的内容为： " + result);
            }
        } catch (
                ClientProtocolException e) {
            //打印 客户端协议异常
            e.printStackTrace();
        } catch (
                ParseException e) {
            //打印 解析异常
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //释放资源
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
        return result;
    }



}
