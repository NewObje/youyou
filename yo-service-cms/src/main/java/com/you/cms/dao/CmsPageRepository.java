package com.you.cms.dao;

import com.you.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Michael Liu
 * @create 2020-02-24 10:05
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

    CmsPage findBySiteIdAndPageNameAndPageWebPath(String siteId, String pageName, String pageWebPath);

    /*CmsPage findByPageName(String pageName);

    CmsPage findByPageNameAndPageType(String pageName, String pageType);

    int countBySiteIdAndPageType(String siteId, String pageType);

    Page<CmsPage> findBySiteIdAnAndPageType(String siteId, String pageType, Pageable pageable);*/
}
