package com.you.video.client;

import com.you.domain.filesystem.FileSystemEntity;
import com.you.vo.RestResultVO;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Michael Liu
 * @create 2020-03-21 13:01
 */
@FeignClient(value = "YO-SERVICE-FILESYSTEM", configuration = FileRemoteClient.FormSupportConfig.class)
public interface FileRemoteClient {

    /**
     * 视频封面上传
     * @param fileTag
     * @param businessKey
     * @param metaData
     * @param file
     * @return
     */
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RestResultVO<FileSystemEntity> upload(@RequestParam("fileTag") String fileTag,
                                                 @RequestParam("businessKey") String businessKey,
                                                 @RequestParam(value = "metaData", required = false) String metaData,
                                                 @RequestPart("file") MultipartFile file);

    class FormSupportConfig {
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;
        // new一个form编码器，实现支持form表单提交
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder(new SpringEncoder(messageConverters));
        }
        // 开启Feign的日志
        @Bean
        public Logger.Level logger() {
            return Logger.Level.FULL;
        }
    }
}
