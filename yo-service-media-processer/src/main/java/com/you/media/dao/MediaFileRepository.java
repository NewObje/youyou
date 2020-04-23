package com.you.media.dao;

import com.you.domain.media.MediaFileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-03-18 11:19
 */
public interface MediaFileRepository extends MongoRepository<MediaFileEntity, String> {
}
