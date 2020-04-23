package com.you.media.web.controller;

import com.you.api.media.MediaUploadControllerApi;
import com.you.media.service.MediaService;
import com.you.vo.RestResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Michael Liu
 * @create 2020-03-17 22:44
 */
@RestController
@RequestMapping("media")
public class MediaController implements MediaUploadControllerApi {

    @Autowired
    MediaService mediaService;

    /**
     * 注册
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimeType
     * @param fileExt
     * @return
     */
    @Override
    @RequestMapping(value = "/upload/register", method = RequestMethod.POST)
    public RestResultVO<String> register(@RequestParam("fileMd5") String fileMd5,
                                         @RequestParam("fileExt") String fileExt,
                                         @RequestParam("fileName") String fileName,
                                         @RequestParam("fileSize") Long fileSize,
                                         @RequestParam("mimeType") String mimeType) {
        String msg = mediaService.register(fileMd5, fileExt, fileName, fileSize, mimeType);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 检查分块文件
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    @Override
    @RequestMapping(value = "/upload/check", method = RequestMethod.POST)
    public RestResultVO<String> checkChunk(@RequestParam("fileMd5") String fileMd5,
                                           @RequestParam("chunk") Integer chunk,
                                           @RequestParam("chunkSize") Integer chunkSize) {
        boolean msg = mediaService.checkChunk(fileMd5, chunk, chunkSize);
        return RestResultVO.ok("操作成功!", msg);
    }

    /**
     * 上传分块
     * @param file
     * @param chunk
     * @param fileMd5
     * @return
     */
    @Override
    @RequestMapping(value = "/upload/uploadchunks", method = RequestMethod.POST)
    public RestResultVO<String> uploadChunks(@RequestParam("file") MultipartFile file,
                                             @RequestParam("chunk") Integer chunk,
                                             @RequestParam("fileMd5") String fileMd5) {
        String msg = mediaService.uploadChunk(file, fileMd5, chunk);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 合并分块
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimeType
     * @param fileExt
     * @return
     */
    @Override
    @RequestMapping(value = "/upload/merge", method = RequestMethod.POST)
    public RestResultVO<String> mergeChunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("fileSize") Long fileSize,
                                    @RequestParam("mimeType") String mimeType,
                                    @RequestParam("fileExt") String fileExt) {
        String fileId = mediaService.mergeChunks(fileName, fileMd5, fileSize, mimeType, fileExt);
        return RestResultVO.ok(fileId, true);
    }
}
