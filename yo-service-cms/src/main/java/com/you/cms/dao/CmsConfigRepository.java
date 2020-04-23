package com.you.cms.dao;

import com.you.domain.cms.CmsConfigEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:30
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfigEntity, String> {
}
