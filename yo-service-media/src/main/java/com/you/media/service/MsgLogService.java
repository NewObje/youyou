package com.you.media.service;

import com.you.domain.log.MsgLogEntity;
import com.you.media.dao.MsgLogRepository;
import com.you.utils.JodaTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-03-24 18:16
 */
@Service
public class MsgLogService {

    @Autowired
    private MsgLogRepository msgLogRepository;

    /**
     * 更新消息处理状态
     * @param msgId
     * @param status
     */
    public void updateMsgLog(String msgId, Integer status) {
        Optional<MsgLogEntity> msgLogOptinal = msgLogRepository.findById(msgId);
        if (msgLogOptinal.isPresent()) {
            MsgLogEntity msgLogEntity = msgLogOptinal.get();
            msgLogEntity.setStatus(status);
            msgLogEntity.setUpdateTime(new Date());
            msgLogRepository.save(msgLogEntity);
        }
    }

    /**
     *  从数据库查询状态为0和下次重试时间小于当前时间的消息
     * @param status
     * @param date
     * @return
     */
    public List<MsgLogEntity> findByStatusAndNextTime(Integer status, Date date) {
        return msgLogRepository.findMsgLogEntitiesByStatusAndNextTryTimeLessThan(status, date);
    }

    /**
     * 更新重试次数
     * @param msgId
     * @param nextTryTime
     */
    public void updateTryCount(String msgId, Date nextTryTime) {
        Optional<MsgLogEntity> optional = msgLogRepository.findById(msgId);
        if (optional.isPresent()) {
            MsgLogEntity msgLogEntity = optional.get();
            msgLogEntity.setTryCount(msgLogEntity.getTryCount() + 1);

            Date date = JodaTimeUtil.plusMinutes(nextTryTime, 1);
            msgLogEntity.setNextTryTime(date);

            msgLogEntity.setUpdateTime(new Date());
            msgLogRepository.save(msgLogEntity);
        }
    }
}
