package com.you.api.video;

import com.you.dto.video.VideoQueryByIdVO;
import com.you.dto.video.VideoQueryDTO;
import com.you.dto.video.VideoQueryVO;
import com.you.vo.RestResultVO;
import com.you.vo.video.PlayVideoVO;
import com.you.vo.video.VideoModelVO;
import com.you.vo.video.VideoPreviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:18
 */
@Api(value = "视频管理接口", description = "视频查询、新增、修改、删除")
public interface VideoManageControllerApi {

    /**
     * 查询视频列表信息
     * @return
     */
    @ApiOperation("查询视频列表")
    public RestResultVO<Map<String, Object>> listVideos(VideoQueryDTO videoQueryDTO);

    /**
     * 后台管理员上传视频
     * @param file
     * @param videoName
     * @param tagId
     * @param videoDesc
     * @param videoId
     * @return
     */
    @ApiOperation("后台管理员上传视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoName", value = "视频名称"),
            @ApiImplicitParam(name = "tagId", value = "视频标签Id"),
            @ApiImplicitParam(name = "videoDesc", value = "视频描述"),
            @ApiImplicitParam(name = "videoId", value = "视频Id")
    })
    public RestResultVO<String> upload(MultipartFile file, String videoName, Integer tagId, String videoDesc, String videoId);

    /**
     * 删除视频
     * @param fileId
     * @return
     */
    @ApiOperation("删除视频")
    @ApiImplicitParam(name = "fileId", value = "视频Id", required = true, paramType = "path", dataType = "String")
    public RestResultVO<String> delete(String fileId);

    /**
     * 修改视频
     * @param file
     * @param videoName
     * @param tagId
     * @param videoDesc
     * @return
     */
    @ApiOperation("修改视频")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoNum", value = "视频名称"),
            @ApiImplicitParam(name = "tagId", value = "视频标签Id"),
            @ApiImplicitParam(name = "videoId", value = "视频Id"),
            @ApiImplicitParam(name = "videoDesc", value = "视频描述")
    })
    public RestResultVO<String> editVideo(MultipartFile file, String videoName, Integer tagId, String videoId, String videoDesc);

    /**
     * 通过Id查询视频信息
     * @param fileId
     * @return
     */
    @ApiOperation("通过Id查询视频信息")
    @ApiImplicitParam(name = "fileId", value = "视频Id", required = true, paramType = "path", dataType = "String")
    public RestResultVO<VideoQueryByIdVO> findById(String fileId);

    /**
     * 通过Id查询视频数据模型信息
     * @param videoId
     * @return
     */
    @ApiOperation("通过Id查询视频数据模型信息")
    @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, paramType = "path", dataType = "String")
    public RestResultVO<VideoModelVO> findVideoModelById(String videoId);

    /**
     * 添加视频页面到cms
     * @param videoId
     * @return
     */
    @ApiOperation("视频页面预览")
    @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, paramType = "path", dataType = "String")
    public RestResultVO<String> addVideoPageToCms(String videoId);

    /**
     * 发布视频页面
     * @param videoId
     * @return
     */
    @ApiOperation("发布视频页面")
    @ApiImplicitParam(name = "videoId", value = "视频Id", required = true, paramType = "path", dataType = "String")
    public RestResultVO<String> postVideoPage(String videoId);

    /**
     * 首页查询视频列表
     * @return
     */
    @ApiOperation("首页查询视频列表")
    public RestResultVO<Map<String, List<PlayVideoVO>>> listIndexVideos();
}
