package com.you.api.filesystem;

import com.you.domain.filesystem.FileSystemEntity;
import com.you.domain.filesystem.response.UploadFileResult;
import com.you.vo.RestResultVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Michael Liu
 * @create 2020-02-27 16:22
 */
public interface FileUploadControllerApi {

    /**
     * 上传模板
     * @param file
     * @return
     */
    @ApiOperation("上传模板文件")
    public String uploadTemp(MultipartFile file);

    /**
     * 删除模板
     * @param fileId
     * @return
     */
    @ApiOperation("删除模板文件")
    @ApiImplicitParam(name = "fileId", value = "文件Id")
    public boolean deleteTemp(String fileId);

    /**
     * 上传文件图片
     * @param file
     * @param fileTag
     * @param businessKey
     * @param metaData
     * @return
     */
    @ApiOperation("上传视频图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileTag", value = "图片标签"),
            @ApiImplicitParam(name = "businessKey", value = "businessKey"),
            @ApiImplicitParam(name = "metaData", value = "图片元数据")
    })
    public RestResultVO<FileSystemEntity> upload(String fileTag,
                                                 String businessKey,
                                                 String metaData,
                                                 MultipartFile file);
}
