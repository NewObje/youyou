package com.you.vo.cms;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-03-01 22:31
 */
@Data
@ToString
@ApiModel(value = "CMS模板VO")
public class CmsTemplateVO extends BaseVO {

    //站点ID
    @ApiModelProperty("站点id")
    private String siteId;

    //站点名称
    @ApiModelProperty("站点名称")
    private String siteName;

    //模版ID
    @ApiModelProperty("模板id")
    private String templateId;

    //模版名称
    @ApiModelProperty("模板名")
    private String templateName;

    //模版参数
    @ApiModelProperty("模板参数")
    private String templateParameter;

    //模版文件Id
    @ApiModelProperty("模版文件Id")
    private String templateFileId;
}
