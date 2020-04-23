package com.you.video.web.controller;

import com.you.api.video.VideoManageControllerApi;
import com.you.dto.video.VideoQueryByIdVO;
import com.you.dto.video.VideoQueryDTO;
import com.you.dto.video.VideoQueryVO;
import com.you.video.service.VideoManageService;
import com.you.vo.RestResultVO;
import com.you.vo.video.PlayVideoVO;
import com.you.vo.video.VideoModelVO;
import com.you.vo.video.VideoPreviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:20
 */
@RestController
@RequestMapping("video")
public class VideoManageController implements VideoManageControllerApi {

    @Autowired
    VideoManageService videoManageService;

    /**
     * 查询视频信息列表
     * @return
     */
    @Override
    @RequestMapping(value = "/listVideos", method = RequestMethod.POST)
    public RestResultVO<Map<String, Object>> listVideos(@RequestBody VideoQueryDTO videoQueryDTO) {
        Map<String, Object> resultMap = videoManageService.listVideos(videoQueryDTO);
        return RestResultVO.ok(resultMap, true);
    }

    /**
     * 后台管理员上传视频
     * @param file
     * @param videoName
     * @param tagId
     * @param videoDesc
     * @return
     */
    @Override
    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
    public RestResultVO<String> upload(@RequestPart("file") MultipartFile file,
                                       @RequestParam String videoName,
                                       @RequestParam Integer tagId,
                                       @RequestParam String videoDesc,
                                       @RequestParam String videoId) {
        String msg = videoManageService.uploadVideo(file, videoName, tagId, videoDesc, videoId);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 删除视频
     * @param fileId
     * @return
     */
    @Override
    @RequestMapping(value = "/delVideoById/{fileId}", method = RequestMethod.GET)
    public RestResultVO<String> delete(@PathVariable("fileId") String fileId) {
        String msg = videoManageService.deleteById(fileId);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 修改视频
     * @param file
     * @param videoName
     * @param tagId
     * @param videoId
     * @param videoDesc
     * @return
     */
    @Override
    @RequestMapping(value = "/editVideo", method = RequestMethod.POST)
    public RestResultVO<String> editVideo(@RequestParam("file") MultipartFile file,
                                          @RequestParam("videoName") String videoName,
                                          @RequestParam("tagId") Integer tagId,
                                          @RequestParam("videoId") String videoId,
                                          @RequestParam("videoDesc") String videoDesc) {
        String msg = videoManageService.editVideo(file, videoName, tagId, videoId, videoDesc);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 通过Id查询视频信息
     * @param fileId
     * @return
     */
    @Override
    @RequestMapping(value = "/findById/{fileId}", method = RequestMethod.GET)
    public RestResultVO<VideoQueryByIdVO> findById(@PathVariable("fileId") String fileId) {
        VideoQueryByIdVO vo = videoManageService.findById(fileId);
        return RestResultVO.ok(vo, true);
    }

    /**
     * 通过Id查询视频数据模型信息
     * @param videoId
     * @return
     */
    @Override
    @RequestMapping(value = "/findVideoModelById/{videoId}", method = RequestMethod.GET)
    public RestResultVO<VideoModelVO> findVideoModelById(@PathVariable("videoId") String videoId) {
        VideoModelVO vo = videoManageService.findVideoModelById(videoId);
        return RestResultVO.ok(vo);
    }

    /**
     * 添加视频页面到cms
     * @param videoId
     * @return
     */
    @Override
    @RequestMapping(value = "/addVideoPage/{videoId}", method = RequestMethod.GET)
    public RestResultVO<String> addVideoPageToCms(@PathVariable("videoId")String videoId) {
        String videoReviewUrl = videoManageService.addVideoPage(videoId);
        if ("添加失败！".equals(videoReviewUrl)) {
            return RestResultVO.ok(videoReviewUrl, false);
        }
        return RestResultVO.ok(videoReviewUrl, true);
    }

    /**
     * 发布视频页面
     * @param videoId
     * @return
     */
    @Override
    @RequestMapping(value = "/postVideo/{videoId}", method = RequestMethod.GET)
    public RestResultVO<String> postVideoPage(@PathVariable("videoId") String videoId) {
        String msg = videoManageService.postVideoPage(videoId);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 首页查询视频列表
     * @return
     */
    @Override
    @RequestMapping(value = "/listIndexVideos", method = RequestMethod.GET)
    public RestResultVO<Map<String, List<PlayVideoVO>>> listIndexVideos() {
        Map<String, List<PlayVideoVO>> resultMap = videoManageService.listIndexVideos();
        return RestResultVO.ok(resultMap);
    }
}
