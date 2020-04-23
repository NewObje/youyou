package com.you.vo.video;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-03-26 23:45
 */
@Data
@ToString
@ApiModel(value = "视频ES查询VO")
public class VideoEsVO extends BaseVO {

    /**
     * 视频id
     */
    @ApiModelProperty("视频id")
    private String id;

    /**
     * 视频标签ID
     */
    @ApiModelProperty("视频标签ID")
    private Integer tagId;

    /**
     * 视频名
     */
    @ApiModelProperty("视频名")
    private String videoName;

    /**
     * 视频描述
     */
    @ApiModelProperty("视频描述")
    private String videoDesc;

    /**
     * 视频封面ID/路径
     */
    @ApiModelProperty("视频封面ID/路径")
    private String picId;

    /**
     * 视频路径URL
     */
    @ApiModelProperty("视频路径URL")
    private String videoUrl;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private String uploadTime;
}
