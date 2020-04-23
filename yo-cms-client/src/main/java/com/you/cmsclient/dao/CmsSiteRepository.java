package com.you.cmsclient.dao;

import com.you.domain.cms.CmsSiteEnity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-03-05 17:35
 */
public interface CmsSiteRepository extends MongoRepository<CmsSiteEnity, String> {
}
