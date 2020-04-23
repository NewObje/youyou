package com.you.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Michael Liu
 * @create 2020-03-17 13:47
 */
@EnableDiscoveryClient // 从Eureka Server获取服务
@SpringBootApplication // 扫描所在包及子包的bean，注入到ioc中
@EntityScan(basePackages = {"com.you.domain.media", "com.you.domain.log"})//扫描实体类
@ComponentScan(basePackages={"com.you.api"})//扫描接口
@ComponentScan(basePackages={"com.you"})//扫描framework中通用类
@ComponentScan(basePackages={"com.you.media", "com.you.media.task"})//扫描本项目下的所有类
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }
}
