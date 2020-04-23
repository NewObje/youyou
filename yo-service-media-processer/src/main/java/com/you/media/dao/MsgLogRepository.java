package com.you.media.dao;

import com.you.domain.log.MsgLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Michael Liu
 * @create 2020-03-24 18:08
 */
public interface MsgLogRepository extends JpaRepository<MsgLogEntity, String> {
}
