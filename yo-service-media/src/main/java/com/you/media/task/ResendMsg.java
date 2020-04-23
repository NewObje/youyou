package com.you.media.task;

import com.you.Common.Constant;
import com.you.domain.log.MsgLogEntity;
import com.you.media.service.MsgLogService;
import com.you.mq.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-25 13:21
 */
@Component
@EnableScheduling
public class ResendMsg {

    public static final Logger LOGGER = LoggerFactory.getLogger(ResendMsg.class);

    @Autowired
    private MsgLogService msgLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 最大重试次数
    public static final int MAX_TRY_COUNT = 3;

    /**
     * 每30s拉取投递失败的消息, 重新投递
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void reSend() {
        LOGGER.info("开始执行重新投递消息定时任务...");

        List<MsgLogEntity> logEntities = msgLogService.findByStatusAndNextTime(0, new Date());
        logEntities.forEach(msgLogEntity -> {
            String msgId = msgLogEntity.getMsgId();

            if (msgLogEntity.getTryCount() >= MAX_TRY_COUNT) {
                msgLogService.updateMsgLog(msgId, Constant.MsgLogStatus.DELIVER_FAIL);
                LOGGER.error("超过最大重试次数，消息投递失败，msgId: {}", msgId);
            } else {
                // 重试次数加 1
                msgLogService.updateTryCount(msgId, msgLogEntity.getNextTryTime());

                CorrelationData correlationData = new CorrelationData(msgId);
                // 重新投递
                rabbitTemplate.convertAndSend(msgLogEntity.getExchange(),
                        msgLogEntity.getRoutingKey(),
                        MessageHelper.objToMsg(msgLogEntity.getMsg()),
                        correlationData);

                LOGGER.info("第: " + msgLogEntity.getTryCount() + 1 + "次投递。");
            }
        });
        LOGGER.info("定时任务结束。");
    }
}
