package com.you.cmsclient.dao;

import com.you.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-03-05 17:35
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}
