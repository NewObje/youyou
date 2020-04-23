package com.you.media.service;

import com.you.domain.log.MsgLogEntity;
import com.you.media.dao.MsgLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-03-24 18:16
 */
@Service
public class MsgLogService {

    @Autowired
    MsgLogRepository msgLogRepository;

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
     * 通过Id查询消息信息
     * @param msgId
     * @return
     */
    public MsgLogEntity findById(String msgId) {
        Optional<MsgLogEntity> optional = msgLogRepository.findById(msgId);
        if (!optional.isPresent()) {
             return null;
        }
        return optional.get();
    }
}
