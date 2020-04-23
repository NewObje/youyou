package com.you.cms.dao;

import com.you.domain.cms.CmsSiteEnity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:30
 */
public interface CmsSiteRepository extends MongoRepository<CmsSiteEnity, String> {
    CmsSiteEnity findBySiteNameAndSiteDomainAndSitePort(String siteName, String siteDomain, String sitePort);
}
