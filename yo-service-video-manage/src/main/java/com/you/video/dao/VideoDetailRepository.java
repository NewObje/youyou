package com.you.video.dao;

import com.you.domain.video.VideoDetailEntity;
import com.you.domain.video.VideoPicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-22 15:21
 */
public interface VideoDetailRepository extends JpaRepository<VideoDetailEntity, String>,
        JpaSpecificationExecutor<VideoDetailEntity> {
    List<VideoDetailEntity> findByTagId(Integer tagId);
}
