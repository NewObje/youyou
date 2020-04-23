package com.you.video.dao;

import com.you.domain.media.MediaFileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:08
 */
public interface VideoRepository extends MongoRepository<MediaFileEntity, String> {
}
