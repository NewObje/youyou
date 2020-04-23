package com.you.media.service;

import com.alibaba.fastjson.JSON;
import com.you.Common.Constant;
import com.you.domain.log.MsgLogEntity;
import com.you.domain.media.MediaFileEntity;
import com.you.domain.media.response.MediaCode;
import com.you.exception.ExceptionCast;
import com.you.media.config.RabbitMqConfig;
import com.you.media.dao.MediaRepository;
import com.you.media.dao.MsgLogRepository;
import com.you.mq.MessageHelper;
import com.you.utils.RandomUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @author Michael Liu
 * @create 2020-03-17 17:57
 */
@Service
public class MediaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaService.class);

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MsgLogRepository msgLogRepository;

    @Value("${yo-service-media.mq.routingkey-media-video}")
    private String routingkey_media_video;

    @Value("${yo-service-media.upload-location}")
    private String uploadPath;

    /**
     * 文件上传注册，检查文件是否已上传过
     * @param fileMD5
     * @param fileExt
     * @param fileName
     * @param fileSize
     * @param mimeType
     * @return
     */
    public String register(String fileMD5, String fileExt, String fileName, Long fileSize, String mimeType) {
        String filePath = getFilePath(fileMD5, fileExt);
        File file = new File(filePath);
        Optional<MediaFileEntity> optional = mediaRepository.findById(fileMD5);
        if (file.exists() && optional.isPresent()) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        // 创建文件目录
        boolean b = createFileFolder(fileMD5);
        if (!b) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        return "操作成功!!";
    }

    /**
     * 检查分块文件是否存在
     * @param fileMD5
     * @param chunk
     * @param chunkSize
     * @return
     */
    public boolean checkChunk(String fileMD5, Integer chunk, Integer chunkSize) {
        // 获取块文件目录路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);
        // 块文件的文件名称以1,2,3..序号命名，没有扩展名
        File file = new File(chunkFileFolderPath + chunk);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 上传分块
     * @param file
     * @param fileMD5
     * @param chunk
     * @return
     */
    public String uploadChunk(MultipartFile file, String fileMD5, Integer chunk) {
        if (null == file) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_NOT_EXIST);
        }
        // 创建分块文件目录
        creatChunkFileFolder(fileMD5);
        // 创建分块文件对象
        File chunkFile = new File(getChunkFileFolderPath(fileMD5) + chunk);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("upload chunkFile fail :{}", e.getMessage());
            ExceptionCast.cast(MediaCode.CHUNK_FILE_EXIST_CHECK);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "上传成功!";
    }

    /**
     * 合并分块文件
     * @param fileName
     * @param fileMD5
     * @param fileSize
     * @param mimeType
     * @param fileExt
     * @return
     */
    public String mergeChunks(String fileName, String fileMD5, Long fileSize, String mimeType, String fileExt) {
        // 获取分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }
        // 合并文件路径
        File mergeFile = new File(getFilePath(fileMD5, fileExt));
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("mergeChunks..create mergeFile failed: {}", e.getMessage());
        }
        if (!newFile) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CREATE_FAILED);
        }
        // 获取分块文件集合
        List<File> chunkFiles = getChunkFiles(chunkFileFolder);

        // 合并分块文件
        mergeFile = mergeFile(mergeFile, chunkFiles);
        if (null == mergeFile) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        // 校验文件MD5
        boolean checkResult = this.checkFileMD5(mergeFile, fileMD5);
        if (!checkResult) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        // 将文件信息存入数据库
        MediaFileEntity fileEntity = new MediaFileEntity();
        fileEntity.setFileSize(fileSize);
        fileEntity.setFileName(fileMD5 + "." + fileExt);
        fileEntity.setUploadTime(new Date());
        fileEntity.setMimeType(mimeType);
        fileEntity.setFileType(fileExt);
        fileEntity.setFileId(fileMD5);
        fileEntity.setFileOriginalName(fileName);
        fileEntity.setFilePath(getFileFolderRelativePath(fileMD5, fileExt));
        // 状态：上传成功
        fileEntity.setFileStatus("301002");
        MediaFileEntity entity = mediaRepository.save(fileEntity);
        // 发送视频处理消息
        sendProcessVideoMsg(fileEntity);
        return entity.getFileId();
    }

    /**
     * 发送视频处理消息给MQ
     * @param fileEntity
     * @return
     */
    private void sendProcessVideoMsg(MediaFileEntity fileEntity) {
        Map<String, String> map = new HashMap<>();
        map.put("mediaId", fileEntity.getFileId());
        // 发送消息Id，消费者进行消息重复消费处理
        String msgId = RandomUtil.UUID32();
        map.put("msgId", msgId);
        String msg = JSON.toJSONString(map);
        MsgLogEntity msgLogEntity = new MsgLogEntity(msgId, map, RabbitMqConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video);
        msgLogRepository.save(msgLogEntity);

        CorrelationData correlationData = new CorrelationData(msgId);
        // 发送消息
        try {
            rabbitTemplate.convertAndSend(RabbitMqConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, MessageHelper.objToMsg(map), correlationData);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分块文件集合
     * @param chunkFileFolder
     * @return
     */
    private List<File> getChunkFiles(File chunkFileFolder) {
        File[] chunkFiles = chunkFileFolder.listFiles();
        List<File> chunkFileList = new ArrayList<>(Arrays.asList(chunkFiles));

        //按分块文件名称排序 升序
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        return chunkFileList;
    }

    /**
     * 校验文件MD5
     * @param mergeFile
     * @param fileMD5
     * @return
     */
    private boolean checkFileMD5(File mergeFile, String fileMD5) {
        if (null == mergeFile && StringUtils.isEmpty(fileMD5)) {
            return false;
        }
        // 进行MD5 校验
        FileInputStream mergeFileInputStream = null;
        try {
            mergeFileInputStream = new FileInputStream(mergeFile);
            // 获取合并文件MD5
            String mergeFileMD5 = DigestUtils.md5Hex(mergeFileInputStream);
            if (fileMD5.equalsIgnoreCase(mergeFileMD5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("check fileMD5 error, file is : {}, MD5 is : {}", mergeFile.getAbsoluteFile(), fileMD5);
        } finally {
            try {
                mergeFileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 合并分块文件
     * @param mergeFile
     * @param chunkFileList
     * @return
     */
    private File mergeFile(File mergeFile, List<File> chunkFileList) {
        try {
            // 创建写入文件对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            byte[] bytes = new byte[1024];
            for (File chunkFile : chunkFileList) {
                // 创建读取文件对象
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                while ((len = raf_read.read(bytes)) != -1) {
                    // 向合并文件中写入
                    raf_write.write(bytes, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("merge file error : {}", e.getMessage());
            return null;
        }
        return mergeFile;
    }

    /**
     * 创建分块文件目录
     * @param fileMD5
     * @return
     */
    private boolean creatChunkFileFolder(String fileMD5) {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);
        File file = new File(chunkFileFolderPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 获取文件分块目录
     * @param fileMD5
     * @return
     */
    private String getChunkFileFolderPath(String fileMD5) {
        return getFileFolderPath(fileMD5) + "/chunks/";
    }

    /**
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     * @param fileMD5 文件md5值
     * @param fileExt 文件扩展名
     * @return 文件路径
     */
    private String getFilePath(String fileMD5, String fileExt) {
        String filePath = uploadPath + fileMD5.substring(0, 1) + "/" + fileMD5.substring(1, 2) + "/" + fileMD5 + "/" + fileMD5 + "." + fileExt;
        return filePath;
    }

    /**
     * 获取文件相对路径
     * @param fileMD5
     * @param fileExt
     * @return
     */
    private String getFileFolderRelativePath(String fileMD5, String fileExt) {
        String filePath = fileMD5.substring(0, 1) + "/" + fileMD5.substring(1, 2) + "/" + fileMD5 + "/";
        return filePath;
    }

    /**
     * 获取文件所在目录
     * @param fileMD5
     * @return
     */
    private String getFileFolderPath(String fileMD5) {
        String fileFolderPath = uploadPath + fileMD5.substring(0, 1) + "/" + fileMD5.substring(1, 2) + "/" + fileMD5 + "/";
        return fileFolderPath;
    }

    /**
     * 创建文件目录
     * @param fileMD5
     * @return
     */
    private boolean createFileFolder(String fileMD5) {
        String fileFolderPath = getFileFolderPath(fileMD5);
        File file = new File(fileFolderPath);
        if (!file.exists()) {
            // 创建文件夹
            return file.mkdirs();
        }
        return true;
    }
}
