package com.you.vo.tag;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-03-20 0:09
 */
@Data
@ToString
@ApiModel(value = "视频标签VO")
public class TagVO extends BaseVO {
    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Integer id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;
}
