package com.you.dto.tag;

import com.you.dto.RequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-03-26 13:36
 */
@Data
@ToString
@ApiModel(value = "视频标签更新DTO")
public class TagUpdateDTO extends RequestDTO {

    /**
     * 标签Id
     */
    @ApiModelProperty("标签Id")
    private Integer id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    private String tagName;
}
