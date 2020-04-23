package com.you.media.dao;

import com.you.domain.media.MediaFileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-03-17 17:56
 */
public interface MediaRepository extends MongoRepository<MediaFileEntity, String> {
}
