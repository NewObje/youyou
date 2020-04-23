package com.you.video.service;

import com.you.domain.cms.CmsPage;
import com.you.domain.media.MediaFileEntity;
import com.you.video.client.CmsClient;
import com.you.video.client.FileRemoteClient;
import com.you.video.dao.VideoRepository;
import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsSiteVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:41
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class VideoManageServiceTest {

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    FileRemoteClient remoteClient;

    @Autowired
    CmsClient cmsClient;

    @Test
    public void testRibbon() {
        String serviceId = "YO-SERVICE-CMS-MANAGE";
        ResponseEntity<CmsPage> entity = restTemplate.getForEntity("http://" + serviceId + "/cms/findPage/5ad92e9068db52404cad0f79", CmsPage.class);
        System.out.println(entity.getBody());
    }

    @Test
    public void testFindAll() {
        /*List<MediaFileEntity> entities = videoRepository.findAll();
        System.out.println(entities.toString());*/
    }
}
