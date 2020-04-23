package com.you.video;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.servlet.MultipartConfigElement;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:03
 */
@EnableFeignClients(basePackages = "com.you.video.client")
@EnableDiscoveryClient // 从Eureka Server获取服务
@SpringBootApplication // 扫描所在包及子包的bean，注入到ioc中
@EntityScan(basePackages = {"com.you.domain.media", "com.you.domain.tag", "com.you.domain.video"})//扫描实体类
@ComponentScan(basePackages={"com.you.api"})//扫描接口
@ComponentScan(basePackages={"com.you"})//扫描framework中通用类
@ComponentScan(basePackages={"com.you.video"})//扫描本项目下的所有类
public class VideoManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoManageApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
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
