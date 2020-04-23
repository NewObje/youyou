package com.you.media.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Liu
 * @create 2020-03-18 10:58
 */
@Configuration
public class RabbitMqConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    private static final String EX_MEDIA_PROCESS_TASK = "ex_media_processor";

    // 默认消息消费者数量
    private static final int DEFAULL_CURRENT_NUM = 10;

    @Value("${yo-service-media.mq.queue-media-video-processor}")
    public String queue_media_video_processtask;

    @Value("${yo-service-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    /**
     * 声明交换机
     * @return
     */
    @Bean(EX_MEDIA_PROCESS_TASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESS_TASK).durable(true).build();
    }

    /**
     * 声明队列
     * @return
     */
    @Bean("queue_media_video_processtask")
    public Queue QUEUE_PROCESSTASK() {
        return new Queue(queue_media_video_processtask, true, false, true);
    }

    /**
     * 队列绑定交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding binding_queue_media_processtask(@Qualifier("queue_media_video_processtask") Queue queue,
                                                   @Qualifier(EX_MEDIA_PROCESS_TASK) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey_media_video).noargs();
    }

    /**
     * 配置容器工厂
     * @param configurer
     * @param factory
     * @return
     */
    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                                                 ConnectionFactory factory) {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConcurrentConsumers(DEFAULL_CURRENT_NUM);
        containerFactory.setMaxConcurrentConsumers(DEFAULL_CURRENT_NUM);
        configurer.configure(containerFactory, factory);
        return containerFactory;
    }

    /**
     * @Description 配置rabbitTemplate
     **/
    @Bean
    RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //配置消息转化器，如果需要在消息通道channel中传输可序列化对象，需要此配置
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
