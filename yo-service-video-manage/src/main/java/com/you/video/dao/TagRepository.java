package com.you.video.dao;

import com.you.domain.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Michael Liu
 * @create 2020-03-20 0:06
 */
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    TagEntity findByTagName(String tagName);
}
