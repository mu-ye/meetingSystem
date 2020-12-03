package com.demo.demo.core.configurer;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * @author mubaisama
 */
@Configuration
public class UpFileConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        //设置文件上传的跟目录
        //factory.setLocation(SystemCommon.BASE_PATH);

        //设置单个文件上传最大尺寸
        factory.setMaxFileSize(DataSize.parse("50MB"));
        //设置 总共上传文件大小
        factory.setMaxRequestSize(DataSize.parse("1024MB"));
        return factory.createMultipartConfig();
    }
}