package com.you.media.listener;

import com.rabbitmq.client.Channel;
import com.you.media.base.Consumer;
import com.you.media.mq.MediaProcessTask;
import com.you.media.proxy.CommonConsumer;
import com.you.media.service.MsgLogService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Michael Liu
 * @create 2020-03-25 20:47
 * 媒资消费监听
 */
@Component
public class MediaListener {

    @Autowired
    private MediaProcessTask mediaProcessTask;

    @Autowired
    private MsgLogService msgLogService;

    @RabbitListener(queues = "${yo-service-media.mq.queue-media-video-processor}",
            containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(Message message, Channel channel) throws IOException {
        CommonConsumer mediaProxy = new CommonConsumer(mediaProcessTask, msgLogService);
        Consumer proxyConsumer = (Consumer) mediaProxy.getObject();

        if (null != proxyConsumer) {
            proxyConsumer.receiveMediaProcessTask(message, channel);
        }
    }
}
