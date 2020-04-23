package com.you.media;

import com.you.domain.media.MediaFileEntity;
import com.you.media.dao.MediaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-24 23:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MediaServiceTest {

    @Autowired
    MediaRepository mediaRepository;

    @Test
    public void test1() {
        List<MediaFileEntity> mediaFileEntityList = mediaRepository.findAll();
        System.out.println(mediaFileEntityList);
    }
}
