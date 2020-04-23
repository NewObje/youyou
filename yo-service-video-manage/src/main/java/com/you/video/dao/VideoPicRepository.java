package com.you.video.dao;

import com.you.domain.video.VideoPicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Michael Liu
 * @create 2020-03-22 15:21
 */
public interface VideoPicRepository extends JpaRepository<VideoPicEntity, String> {
}
