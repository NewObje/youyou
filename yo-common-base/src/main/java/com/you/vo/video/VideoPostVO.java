package com.you.vo.video;

import com.you.vo.BaseVO;
import com.you.vo.RestResultVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-03-31 11:42
 */
@Data
@ToString
@ApiModel(value = "播放视频数据模型VO")
public class VideoPostVO extends BaseVO {
    /**
     * 视频页面正式Url
     */
    @ApiModelProperty("视频页面正式Url")
    private String videoUrl;
}
