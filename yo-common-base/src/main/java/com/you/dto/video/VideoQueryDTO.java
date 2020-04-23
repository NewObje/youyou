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
@ApiModel(value = "视频查询DTO")
public class VideoQueryDTO extends RequestDTO {

    /**
     * 视频名称
     */
    @ApiModelProperty("视频名称")
    private String videoName;

    /**
     * 视频标签Id
     */
    @ApiModelProperty("视频标签Id")
    private Integer tagId;

    /**
     * 上传用户
     */
    @ApiModelProperty("上传用户")
    private String userName;

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
