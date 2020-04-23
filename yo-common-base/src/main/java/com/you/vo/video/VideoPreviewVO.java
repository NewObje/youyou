package com.you.vo.video;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-26 23:45
 */
@Data
@ToString
@ApiModel(value = "视频页面预览VO")
public class VideoPreviewVO extends BaseVO {
    /**
     * 视频页面预览Url
     */
    @ApiModelProperty("视频页面预览Url")
    private String preViewUrl;

}
