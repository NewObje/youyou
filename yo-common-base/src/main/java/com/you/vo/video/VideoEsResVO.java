package com.you.vo.video;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-04-08 14:10
 */
@Data
@ToString
@ApiModel(value = "视频ES查询VO")
public class VideoEsResVO extends BaseVO {
    /**
     * 查询视频结果
     */
    List<VideoEsVO> list;

    /**
     * 条目数
     */
    Long total;
}
