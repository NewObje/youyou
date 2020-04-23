package com.you.file.service;

import com.alibaba.fastjson.JSON;
import com.you.domain.cms.response.CmsCode;
import com.you.domain.filesystem.FileSystemEntity;
import com.you.domain.filesystem.response.FileSystemCode;
import com.you.domain.filesystem.response.UploadFileResult;
import com.you.exception.ExceptionCast;
import com.you.file.dao.FileRepository;
import com.you.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-02-27 16:29
 */
@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public static final String TRACKER_SERVER = "fastfdfs.properties";

    @Autowired
    FileRepository fileRepository;

    @Autowired
    GridFsTemplate gridFsTemplate;

    private static TrackerClient trackerClient = null;

    /**
     * 初始化FastDfs
     */
    static {
        try {
            ClientGlobal.initByProperties(TRACKER_SERVER);
            //创建tracker客户端对象
            trackerClient = new TrackerClient();
        } catch (Exception e) {
            e.printStackTrace();
            // 初始化文件系统失败
            ExceptionCast.cast(FileSystemCode.FS_INIT_FAILED);
        }
    }

    /**
     * 获取StorageClient1
     * @return
     */
    private static StorageClient1 getStorageClient1() {
        try {
            //创建tracker服务端对象
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建storageServer服务端对象
            StorageServer storageServer = null;
            //获得StorageClient1
            return new StorageClient1(trackerServer, storageServer);
        } catch (IOException e) {
            e.printStackTrace();
            // 初始化文件系统失败
            ExceptionCast.cast(FileSystemCode.FS_INIT_FAILED);
        }
        return null;
    }

    /**
     * 上传文件到FDFS
     * @param file
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    public FileSystemEntity upload(MultipartFile file, String filetag, String businesskey, String metadata) {
        if (null == file) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        // 上传文件到fdfs
        String fileId = uploadFileToFdfs(file);
        FileSystemEntity entity = new FileSystemEntity();
        entity.setFileId(fileId);
        entity.setFilePath(fileId);
        entity.setBusinesskey(businesskey);
        entity.setFileName(file.getOriginalFilename());
        entity.setFileSize(file.getSize());
        entity.setFileType(file.getContentType());
        entity.setFiletag(filetag);
        if (StringUtils.isNotEmpty(metadata)) {
            try {
                Map map = JSON.parseObject(metadata, Map.class);
                entity.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileSystemEntity systemEntity = fileRepository.save(entity);
        return systemEntity;
    }

    /**
     * 上传文件到FDFS
     * @param file
     * @return
     */
    private String uploadFileToFdfs(MultipartFile file) {
        try {
            StorageClient1 storageClient1 = getStorageClient1();
            //文件字节
            byte[] bytes = file.getBytes();
            //文件原始名称
            String originalFilename = file.getOriginalFilename();
            //文件扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //文件id
            String fileId = storageClient1.upload_file1(bytes, extName, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 模板文件存储
     * @param file
     * @return
     */
    public String uploadTemp(MultipartFile file) {
        if (file.isEmpty()) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String originalFilename = file.getOriginalFilename();
        String fileName = StringUtils.split(originalFilename, ".")[0];
        try {
            InputStream inputStream1 = file.getInputStream();
            ObjectId fileId = gridFsTemplate.store(inputStream1, fileName, "utf-8");
            //返回模板存储文件ID
            return fileId.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除模板文件
     * @param fileId
     * @return
     */
    public boolean deleteTempById(String fileId) {
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
        return true;
    }
}
