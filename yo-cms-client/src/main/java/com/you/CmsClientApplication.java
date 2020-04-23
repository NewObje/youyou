package com.you;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Michael Liu
 * @create 2020-03-05 16:37
 */
@SpringBootApplication
@EntityScan("com.you.domain.cms") //扫描实体类
@ComponentScan(basePackages = "com.you.cmsclient")
@ComponentScan(basePackages = "com.you")
public class CmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmsClientApplication.class, args);
    }
}
