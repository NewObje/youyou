package com.you.api.tag;

import com.you.dto.tag.TagUpdateDTO;
import com.you.vo.RestResultVO;
import com.you.vo.tag.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-21 11:36
 */
@Api(value = "视频管理接口", description = "视频查询、新增、修改、删除")
public interface TagControllerApi {

    /**
     * 查询所有视频标签
     * @return
     */
    @ApiOperation("查询视频标签列表")
    public RestResultVO<List<TagVO>> listTags();

    /**
     * 通过Id查询视频标签
     * @return
     */
    @ApiOperation("通过Id查询视频标签")
    @ApiImplicitParam(name = "id", value = "标签ID", required = true, paramType = "path", dataType = "Integer")
    public RestResultVO<TagVO> findTagById(Integer id);

    /**
     * 通过Id删除视频标签
     * @return
     */
    @ApiOperation("通过Id删除视频标签")
    @ApiImplicitParam(name = "id", value = "标签ID", required = true, paramType = "path", dataType = "Integer")
    public RestResultVO<String> delTagById(Integer id);

    /**
     * 新增视频标签
     * @return
     */
    @ApiOperation("新增视频标签")
    @ApiImplicitParam(name = "tagName", value = "标签名", required = true, paramType = "path", dataType = "String")
    public RestResultVO<String> addTag(String tagName);

    /**
     * 更新视频标签
     * @return
     */
    @ApiOperation("更新视频标签")
    public RestResultVO<String> updateTag(TagUpdateDTO tagUpdateDTO);
}
