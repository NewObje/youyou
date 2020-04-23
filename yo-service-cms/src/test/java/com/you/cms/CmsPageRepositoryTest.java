package com.you.cms;

import com.you.cms.dao.CmsPageRepository;
import com.you.cms.service.PageService;
import com.you.domain.cms.CmsConfigEntity;
import com.you.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
/**
 * @author Michael Liu
 * @create 2020-02-24 10:07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository repository;

    @Autowired
    PageService pageService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testQuery() {
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        ExampleMatcher matcher = exampleMatcher
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
                //.withMatcher("siteId", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setPageAliase("轮播图");

        Example<CmsPage> example = Example.of(cmsPage, matcher);
        /*List<CmsPage> cmsPages = repository.findAll(example);
        System.out.println(cmsPages.toString());*/

        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("siteId").is("5a751fab6abb5044e0d19ea1");
        criteria.and("pageAliase").is("课程详情页面");
        query.addCriteria(criteria);
        List<CmsPage> cmsPages = mongoTemplate.find(query, CmsPage.class);
        System.out.println(cmsPages.toString());
    }

    @Test
    public void testBuild() {
        String html = pageService.buildPage("5e5f12854bb8f19c78ddfb70");
        System.out.println(html);
    }
}
