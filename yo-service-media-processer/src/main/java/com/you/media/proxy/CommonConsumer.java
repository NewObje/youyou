package com.you.media.proxy;

import com.rabbitmq.client.Channel;
import com.you.Common.Constant;
import com.you.domain.log.MsgLogEntity;
import com.you.media.service.MsgLogService;
import com.you.mq.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-03-25 20:49
 */
public class CommonConsumer {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommonConsumer.class);

    private Object target;

    private MsgLogService msgLogService;

    public CommonConsumer(Object target, MsgLogService msgLogService) {
        this.target = target;
        this.msgLogService = msgLogService;
    }

    public Object getObject() {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();

        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, ((proxy1, method, args) -> {
            Message message = (Message) args[0];
            Channel channel = (Channel) args[1];

            String msgId = getMsgId(message);
            if (isConsumered(msgId)) {
                LOGGER.info("消息重复消费，msgId: {}", msgId);
                return null;
            }

            long tagId = message.getMessageProperties().getDeliveryTag();

            try {
                Object result = method.invoke(target, args);// 真正处理视频方法
                msgLogService.updateMsgLog(msgId, Constant.MsgLogStatus.CONSUMED_SUCCESS);
                // 手动返回ACK
                channel.basicAck(tagId, false);
                return result;
            } catch (Exception e) {
                LOGGER.error("get Proxy error: {}", e);
                channel.basicNack(tagId, false, true);
                return null;
            }
        }));

        return proxy;
    }

    /**
     * 获取消息msgId
     * @param message
     * @return
     */
    private String getMsgId(Message message) {
        String msgId = null;
        Map map = MessageHelper.msgToObj(message, Map.class);
        msgId = (String) map.get("msgId");
        return msgId;
    }

    /**
     * 消息是否被消费
     * @param msgId
     * @return
     */
    private boolean isConsumered (String msgId) {
        MsgLogEntity msgLogEntity = msgLogService.findById(msgId);
        // 消费幂等性
        if (null == msgLogEntity || msgLogEntity.getStatus().equals(Constant.MsgLogStatus.CONSUMED_SUCCESS)) {
            return true;
        }
        return false;
    }

}
