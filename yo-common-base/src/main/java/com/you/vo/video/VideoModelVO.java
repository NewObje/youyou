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
@ApiModel(value = "视频数据模型VO")
public class VideoModelVO extends BaseVO {
    /**
     * 播放视频VO
     */
    @ApiModelProperty("播放视频VO")
    private PlayVideoVO playVideoVO;

    /**
     * 猜你喜欢视频VO列表
     */
    @ApiModelProperty("猜你喜欢视频VO列表")
    private List<PlayVideoVO> playVideoVOList;
}
