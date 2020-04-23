package com.you.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Liu
 * @create 2019-05-26 16:15
 */
@EnableDiscoveryClient
@SpringBootApplication
@EntityScan("com.you.domain.cms") //扫描实体类
@ComponentScan(basePackages = "com.you.api") //扫描接口
@ComponentScan(basePackages = "com.you.cms")
@ComponentScan(basePackages = "com.you")
public class CmsManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsManageApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
