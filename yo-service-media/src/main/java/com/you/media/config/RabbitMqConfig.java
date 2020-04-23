package com.you.media.config;

import com.you.Common.Constant;
import com.you.media.service.MsgLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Michael Liu
 * @create 2020-03-18 10:58
 */
@Configuration
public class RabbitMqConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqConfig.class);

    // 交换机名称
    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";

    @Value("${yo-service-media.mq.queue-media-video-processor}")
    public String queue_media_video_processtask;

    @Value("${yo-service-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    /**
     * 声明交换机
     * @return
     */
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
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
                                                   @Qualifier(EX_MEDIA_PROCESSTASK) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey_media_video).noargs();
    }

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private MsgLogService msgLogService;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());

        // 消息是否成功发送到Exchange
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                LOGGER.info("视频处理消息发送成功！");
                String msgId = correlationData.getId();
                // 消息发送成功
                msgLogService.updateMsgLog(msgId, Constant.MsgLogStatus.DELIVER_SUCCESS);
            } else {
                LOGGER.error("消息发送失败: {}, cause: {}", correlationData, cause);
            }
        }));

        // 触发setReturnCallback回调必须设置mandatory=true, 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        //rabbitTemplate.setMandatory(true);
        // 消息是否从Exchange路由到Queue, 注意: 这是一个失败回调, 只有消息从Exchange路由到Queue失败才会回调这个方法
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {
            LOGGER.info("消息从Exchange路由到Queue失败: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}", exchange, routingKey, replyCode, replyText, message);
        }));

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
}
