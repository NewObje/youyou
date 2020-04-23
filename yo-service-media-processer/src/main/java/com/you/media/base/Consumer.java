package com.you.media.base;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.io.IOException;

/**
 * @author Michael Liu
 * @create 2020-03-25 20:44
 */
public interface Consumer {
    public void receiveMediaProcessTask(Message message, Channel channel) throws IOException;
}
