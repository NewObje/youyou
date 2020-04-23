package com.you.video.web.controller;

import com.you.api.tag.TagControllerApi;
import com.you.domain.tag.TagEntity;
import com.you.dto.tag.TagUpdateDTO;
import com.you.video.service.TagService;
import com.you.vo.RestResultVO;
import com.you.vo.tag.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-20 18:09
 */
@RestController
@RequestMapping("tag")
public class TagController implements TagControllerApi {

    @Autowired
    TagService tagService;

    /**
     * 查询所有视频标签
     * @return
     */
    @Override
    @RequestMapping(value = "/listTags", method = RequestMethod.GET)
    public RestResultVO<List<TagVO>> listTags() {
        List<TagVO> vos = tagService.listTags();
        return RestResultVO.ok(vos);
    }

    /**
     * 通过Id查询视频标签
     * @param id
     * @return
     */
    @Override
    @RequestMapping(value = "/find/{id}", method = RequestMethod.GET)
    public RestResultVO<TagVO> findTagById(@PathVariable("id") Integer id) {
        TagVO vo = tagService.findById(id);
        return RestResultVO.ok(vo);
    }

    /**
     * 通过Id删除视频标签
     * @param id
     * @return
     */
    @Override
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public RestResultVO<String> delTagById(@PathVariable("id") Integer id) {
        String msg = tagService.deleteById(id);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 新增视频标签
     * @param tagName
     * @return
     */
    @Override
    @RequestMapping(value = "/add/{tagName}", method = RequestMethod.GET)
    public RestResultVO<String> addTag(@PathVariable("tagName") String tagName) {
        String msg = tagService.addTag(tagName);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 更新视频标签
     * @param tagUpdateDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultVO<String> updateTag(@RequestBody TagUpdateDTO tagUpdateDTO) {
        String msg = tagService.updateTag(tagUpdateDTO);
        return RestResultVO.ok(msg, true);
    }
}
