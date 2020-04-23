package com.you.dto.video;

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
@ApiModel(value = "通过Id查询视频VO")
public class VideoQueryByIdVO extends BaseVO {

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
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 视频发布状态
     */
    @ApiModelProperty("视频发布状态")
    private Integer status;

    /**
     * 视频url
     */
    @ApiModelProperty("视频url")
    private String videoUrl;
}
