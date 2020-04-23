package com.you.domain.cms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Created by MichaelLiu on 2019/2/6.
 */
@Data
@ToString
public class CmsConfigModel {

    private String key;

    private String name;

    private String url;

    private Map mapValue;

    private String value;

}
