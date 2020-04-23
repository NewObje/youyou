package com.you.media.mq;

import com.rabbitmq.client.Channel;
import com.you.Common.Constant;
import com.you.domain.log.MsgLogEntity;
import com.you.domain.media.MediaFileEntity;
import com.you.domain.media.MediaFileProcess_m3u8;
import com.you.media.base.Consumer;
import com.you.media.dao.MediaFileRepository;
import com.you.media.service.MsgLogService;
import com.you.mq.MessageHelper;
import com.you.utils.HlsVideoUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-03-18 16:29
 */
@Component
public class MediaProcessTask implements Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaProcessTask.class);

    @Value("${yo-service-media.ffmpeg-path}")
    String ffmpeg_path;

    @Value("${yo-service-media.video-location}")
    String videoPath;

    @Autowired
    private MediaFileRepository fileRepository;

    @Override
    public void receiveMediaProcessTask(Message message, Channel channel) throws IOException {
        Map map = MessageHelper.msgToObj(message, Map.class);
        LOGGER.info("received msg media process task  : {}", map);
        // 媒资文件ID
        String mediaId = (String)map.get("mediaId");

        //处理任务
        processTask(mediaId);
    }

    /**
     * 处理任务
     * @param mediaId
     */
    private void processTask(String mediaId) {
        if (StringUtils.isEmpty(mediaId)) {
            return;
        }

        Optional<MediaFileEntity> optional = fileRepository.findById(mediaId);
        if (!optional.isPresent()) {
            return;
        }
        MediaFileEntity mediaFileEntity = optional.get();
        String fileType = mediaFileEntity.getFileType();

        // 只处理avi文件
        if (null == fileType || !"mp4".equals(fileType)) {
            // 无需处理
            mediaFileEntity.setFileStatus("303004");
            fileRepository.save(mediaFileEntity);
            return;
        } else {
            // 未处理
            mediaFileEntity.setFileStatus("301001");
            fileRepository.save(mediaFileEntity);
        }

        String result = "";
        String video_path = "";
        String video_name = "";
        /*// 生成mp4
        video_path = videoPath + mediaFileEntity.getFilePath() + mediaFileEntity.getFileName();
        video_name = mediaFileEntity.getFileId() + ".mp4";
        String videoFolder_path = videoPath + mediaFileEntity.getFilePath();

        Mp4VideoUtil util = new Mp4VideoUtil(ffmpeg_path, video_path, video_name, videoFolder_path);
        result = util.generateMp4();
        if (null == result || !"success".equals(result)) {
            // 处理失败 写入日志
            mediaFileEntity.setFileStatus("303003");
            MediaFileProcess_m3u8 process_m3u8 = new MediaFileProcess_m3u8();
            process_m3u8.setErrormsg(result);
            mediaFileEntity.setMediaFileProcess_m3u8(process_m3u8);
            fileRepository.save(mediaFileEntity);
        }*/

        // 生成m3u8文件
        video_name = mediaFileEntity.getFileId() + ".mp4";
        video_path = videoPath + mediaFileEntity.getFilePath() + video_name;
        String m3u8_name = mediaFileEntity.getFileId() + ".m3u8";
        String m3u8_folder = videoPath + mediaFileEntity.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path, video_path, m3u8_name, m3u8_folder);
        result = hlsVideoUtil.generateM3u8();
        if (null == result || !"success".equals(result)) {
            // 处理失败
            mediaFileEntity.setFileStatus("303003");
            MediaFileProcess_m3u8 m3u8 = new MediaFileProcess_m3u8();
            m3u8.setErrormsg(result);
            mediaFileEntity.setMediaFileProcess_m3u8(m3u8);
            fileRepository.save(mediaFileEntity);
            return;
        }
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        mediaFileEntity.setFileStatus("303002");
        MediaFileProcess_m3u8 m3u8 = new MediaFileProcess_m3u8();
        m3u8.setTslist(ts_list);
        mediaFileEntity.setMediaFileProcess_m3u8(m3u8);
        // m3u8文件Url
        mediaFileEntity.setFileUrl(mediaFileEntity.getFilePath() + "hls/" + m3u8_name);
        fileRepository.save(mediaFileEntity);
    }
}
