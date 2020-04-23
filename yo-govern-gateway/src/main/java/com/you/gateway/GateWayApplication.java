package com.you.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author Michael Liu
 * @create 2020-04-14 21:09
 */
@SpringBootApplication
@EnableZuulProxy //此工程是一个zuul网关
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }
}
