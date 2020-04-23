package com.you.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-03-01 11:59
 */
@Data
@ToString
@ApiModel(value = "CMS配置数据模型信息")
public class CmsConfigModel {

    @ApiModelProperty(value = "模型key", name = "key")
    private String key;

    @ApiModelProperty(value = "模型名", name = "name")
    private String name;

    @ApiModelProperty(value = "数据url", name = "url")
    private String url;

    @ApiModelProperty(value = "数据复杂值", name = "mapValue")
    private Map mapValue;

    @ApiModelProperty(value = "数据简单值", name = "value")
    private String value;
}
