package com.you.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.servlet.MultipartConfigElement;

/**
 * @author Michael Liu
 * @create 2020-02-27 16:43
 */
@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@EntityScan("com.you.domain.filesystem")//扫描实体类
@ComponentScan(basePackages={"com.you.api"})//扫描接口
//@ComponentScan(basePackages={"com.you.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.you.file"})//扫描本项目下的所有类
public class FileApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }

    /**
     * 设置文件上传大小
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5120KB");
        factory.setMaxRequestSize("5120KB");
        return factory.createMultipartConfig();
    }
}


