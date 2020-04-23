package com.you.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Michael Liu
 * @create 2020-03-18 10:42
 */
@SpringBootApplication
@EntityScan(basePackages = {"com.you.domain.media", "com.you.domain.log"})//扫描实体类
@ComponentScan(basePackages={"com.you.api"})//扫描接口
@ComponentScan(basePackages={"com.you.media", "com.you.media.listener"})//扫描本项目下的所有类
@ComponentScan(basePackages={"com.you"})//扫描common下的所有类
public class MediaProcesserAplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaProcesserAplication.class, args);
    }
}
