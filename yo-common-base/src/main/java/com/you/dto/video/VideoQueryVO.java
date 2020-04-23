package com.you.dto.video;

import com.you.dto.RequestDTO;
import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-03-19 20:10
 */
@Data
@ToString
@ApiModel(value = "视频查询VO")
public class VideoQueryVO extends BaseVO {

    /**
     * 文件id、名称、大小、文件类型、文件状态（未上传、上传完成、上传失败）、
     * 上传时间、视频处理方式、视频处理状态
     */

    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 文件原始名称
     */
    @ApiModelProperty("文件原始名称")
    private String fileOriginalName;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    /**
     * 文件url
     */
    @ApiModelProperty("文件url")
    private String fileUrl;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private Long fileSize;

    /**
     * 文件状态
     */
    @ApiModelProperty("文件状态")
    private String fileStatus;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 处理状态
     */
    @ApiModelProperty("处理状态")
    private String processStatus;

    /**
     * tag标签用于查询
     */
    @ApiModelProperty("tag标签")
    private String tag;

    /**
     * 封面路径
     */
    @ApiModelProperty("封面路径")
    private String imgPath;

    /**
     * 上传用户名
     */
    @ApiModelProperty("上传用户名")
    private String userName;

    /**
     * 视频发布状态
     */
    @ApiModelProperty("视频发布状态")
    private Integer status;
}
