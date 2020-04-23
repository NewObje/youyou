package com.you.video.service;

import com.you.domain.tag.TagEntity;
import com.you.video.dao.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:41
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TagServiceTest {

    @Autowired
    TagRepository repository;

    @Test
    public void testFindAll() {
        List<TagEntity> entities = repository.findAll();
        System.out.println(entities.toString());
    }

    @Test
    public void testAdd() {
        TagEntity entity = new TagEntity();
        entity.setId(101);
        entity.setTagName("test");
        TagEntity entity1 = repository.save(entity);
        System.out.println(entity1.toString());
    }

    @Test
    public void testDel() {
        String path = "D:\\Project\\yoyo_video\\8\\0\\809303a36f33b317bc06c7eee60c812c";
        File file = new File(path);
        del(file);
    }

    private void del(File file) {
        File[] files = file.listFiles();
        List<File> list = Arrays.asList(files);
        for (File file1 : list) {
            if (!file1.exists() || null == file) {
                return;
            }
            if (file1.isDirectory()) {
                del(file1);
            } else {
                file1.delete();
            }
        }
        file.delete();
    }
}
