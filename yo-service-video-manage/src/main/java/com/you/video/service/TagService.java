package com.you.video.service;

import com.you.domain.tag.TagEntity;
import com.you.domain.tag.response.TagCode;
import com.you.domain.video.VideoDetailEntity;
import com.you.dto.tag.TagUpdateDTO;
import com.you.exception.ExceptionCast;
import com.you.utils.BeanUtils;
import com.you.utils.CollectionUtils;
import com.you.video.dao.TagRepository;
import com.you.video.dao.VideoDetailRepository;
import com.you.vo.tag.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-03-20 0:07
 */
@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    VideoDetailRepository videoDetailRepository;

    /**
     * 查询所有视频标签
     * @return
     */
    public List<TagVO> listTags() {
        List<TagEntity> tagEntities = tagRepository.findAll();
        List<TagVO> resultList = new ArrayList<>();
        tagEntities.stream().forEach(tagEntity -> {
            resultList.add(BeanUtils.clone(tagEntity, TagVO.class));
        });
        return resultList;
    }

    /**
     * 通过Id查询视频标签
     * @param id
     * @return
     */
    public TagVO findById(Integer id) {
        Optional<TagEntity> optional = tagRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(TagCode.TAG_IS_NOT_EXIST);
        }
        TagVO tagVO = BeanUtils.clone(optional.get(), TagVO.class);
        return tagVO;
    }

    /**
     * 新增视频标签
     * @param tagName
     * @return
     */
    public String addTag(String tagName) {
        TagEntity entity = tagRepository.findByTagName(tagName);
        if (null != entity) {
            ExceptionCast.cast(TagCode.TAG_IS_EXISTED);
        }
        TagEntity tagEntity = new TagEntity();
        tagEntity.setId(null);
        tagEntity.setTagName(tagName);
        tagRepository.save(tagEntity);
        return "操作成功！";
    }

    /**
     * 更新视频标签
     * @param tagUpdateDTO
     * @return
     */
    public String updateTag(TagUpdateDTO tagUpdateDTO) {
        TagVO tagVO = this.findById(tagUpdateDTO.getId());
        tagVO.setTagName(tagUpdateDTO.getTagName());
        TagEntity tagEntity = BeanUtils.clone(tagVO, TagEntity.class);
        tagRepository.save(tagEntity);
        return "操作成功！";
    }

    /**
     * 通过Id删除视频标签
     * @param id
     * @return
     */
    public String deleteById(Integer id) {
        this.findById(id);
        List<VideoDetailEntity> entities = videoDetailRepository.findByTagId(id);
        if (!CollectionUtils.isEmpty(entities)) {
            ExceptionCast.cast(TagCode.TAG_IS_USED_BY_VIDEO);
        }
        tagRepository.deleteById(id);
        return "操作成功！";
    }
}
