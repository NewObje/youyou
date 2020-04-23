package com.you.file.web;

import com.you.api.filesystem.FileUploadControllerApi;
import com.you.domain.filesystem.FileSystemEntity;
import com.you.file.dao.FileRepository;
import com.you.file.service.FileService;
import com.you.vo.RestResultVO;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-02-27 16:23
 */
@RestController
@RequestMapping("file")
public class FileUploadController implements FileUploadControllerApi {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FileService fileService;

    /**
     * 上传模板文件
     * @param file
     * @return
     */
    @Override
    @RequestMapping(value = "/uploadtemp", method = RequestMethod.POST)
    public String uploadTemp(@RequestParam("file") MultipartFile file) {
        String fileId = fileService.uploadTemp(file);
        return fileId;
    }

    /**
     * 删除模板文件
     * @param fileId
     * @return
     */
    @Override
    @RequestMapping(value = "/delete/{fileId}", method = RequestMethod.GET)
    public boolean deleteTemp(@PathVariable("fileId") String fileId) {
        return fileService.deleteTempById(fileId);
    }

    /**
     * 上传视频图片
     * @param file
     * @param fileTag
     * @param businessKey
     * @param metaData
     * @return
     */
    @Override
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RestResultVO<FileSystemEntity> upload(@RequestParam("fileTag") String fileTag,
                                                 @RequestParam("businessKey") String businessKey,
                                                 @RequestParam(value = "metaData", required = false) String metaData,
                                                 @RequestPart("file") MultipartFile file) {
        FileSystemEntity entity = fileService.upload(file, fileTag, businessKey, metaData);
        return RestResultVO.ok(entity, true);
    }
}
