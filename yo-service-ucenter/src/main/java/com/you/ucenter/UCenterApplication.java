package com.you.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Liu
 * @create 2020-04-13 11:45
 */
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.you.ucenter.dao")
@EntityScan("com.you.domain.ucenter")//扫描实体类
@ComponentScan(basePackages={"com.you.api"})//扫描接口
@ComponentScan(basePackages={"com.you", "com.you.ucenter"})//扫描common下的所有类
@SpringBootApplication
public class UCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UCenterApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
