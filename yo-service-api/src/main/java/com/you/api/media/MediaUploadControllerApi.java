package com.you.api.media;

import com.you.vo.RestResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Michael Liu
 * @create 2020-03-17 17:40
 */
@Api(value = "媒资管理接口", description = "视频文件上传、处理")
public interface MediaUploadControllerApi {

    /**
     * 文件注册
     * @return
     */
    @ApiOperation("文件注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMd5", value = "文件MD5", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "fileSize", value = "文件大小", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "mimeType", value = "文件MD5", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "fileExt", value = "文件MD5", required = true, paramType = "path", dataType = "String")
    })
    public RestResultVO<String> register(String fileMd5,
                                         String fileExt,
                                         String fileName,
                                         Long fileSize,
                                         String mimeType);

    /**
     * 检查分块
     * @return
     */
    @ApiOperation("检查分块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMD5", value = "文件MD5", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "chunk", value = "分块", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "chunkSize", value = "分块大小", required = true, paramType = "path", dataType = "Integer")
    })
    public RestResultVO<String> checkChunk(String fileMd5,
                                   Integer chunk,
                                   Integer chunkSize);

    /**
     * 上传分块
     * @return
     */
    @ApiOperation("上传分块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "MultipartFile", value = "文件信息", required = true, paramType = "path", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "chunk", value = "分块", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "fileMD5", value = "文件MD5", required = true, paramType = "path", dataType = "String")
    })
    public RestResultVO<String> uploadChunks(MultipartFile file,
                                     Integer chunk,
                                     String fileMd5);

    /**
     * 合并分块
     * @return
     */
    @ApiOperation("合并分块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMD5", value = "文件MD5", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "fileSize", value = "文件大小", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "mimeType", value = "文件MD5", required = true, paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "fileExt", value = "文件扩展名", required = true, paramType = "path", dataType = "String")
    })
    public RestResultVO<String> mergeChunks(String fileMd5,
                                            String fileName,
                                            Long fileSize,
                                            String mimeType,
                                            String fileExt);
}
