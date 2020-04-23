package com.you.video.dao;

import com.you.domain.video.VideoPubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Michael Liu
 * @create 2020-04-04 11:24
 */
public interface VideoPubRepository extends JpaRepository<VideoPubEntity, String> {
}
