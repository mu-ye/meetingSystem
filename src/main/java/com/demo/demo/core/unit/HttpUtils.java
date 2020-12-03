package com.demo.demo.core.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * Http 工具类
 *
 * @author RCNJTECH
 * @date 2020/4/15 11:15
 */
@Slf4j
public class HttpUtils {

    /**
     * 发送 HTTP GET 请求
     *
     * @param url 请求 URL
     * @return 请求结果
     */
    public static String get(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
        } catch (Exception e) {
            log.error("请求失败：{}", url);
            return null;
        }
    }

    /**
     * 发送 HTTP GET 请求，处理响应编码问题
     *
     * @param url 请求 URL
     * @return 请求结果
     */
    public static String getEncoded(String url) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForEntity(url, String.class).getBody();
            if (result != null) {
                return new String(result.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("请求失败：{}", url);
            return null;
        }
    }

}
