package com.you.vo.cms;

import com.you.model.CmsConfigModel;
import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-01 11:42
 */
@Data
@ToString
@ApiModel(value = "CMS配置VO")
public class CmsConfigVO extends BaseVO {

    @ApiModelProperty(value = "主键ID", name = "id")
    private String id;

    @ApiModelProperty(value = "配置页面名", name = "name")
    private String name;

    @ApiModelProperty(value = "数据模型信息", name = "model")
    private List<CmsConfigModel> model;
}
