package com.you.dto.video;

import com.you.dto.RequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-03-21 12:09
 */
@Data
@ToString
@ApiModel(value = "视频ES查询DTO")
public class VideoEsDTO extends RequestDTO {

    /**
     * 视频名称
     */
    @ApiModelProperty("视频名称")
    private String videoName;

    /**
     * 地区
     */
    @ApiModelProperty("地区")
    private String place;

    /**
     * 剧情
     */
    @ApiModelProperty("剧情")
    private String story;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String year;

    /**
     * 视频标签Id
     */
    @ApiModelProperty("视频标签Id")
    private Integer tagId;

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    private Integer pageNum;

    /**
     * 显示条目
     */
    @ApiModelProperty("显示条目数")
    private Integer pageSize;
}
