package com.you.cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Liu
 * @create 2020-03-05 21:30
 */
@Configuration
public class RabbitmqConfig {
    public static final String EX_ROUTING_CMS_POSTPAGE = "ex_routing_cms_postpage";

    /**
     * 声明队列
     * @return
     */
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EXCANGE_TOPICS_INFORM() {
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }
}
