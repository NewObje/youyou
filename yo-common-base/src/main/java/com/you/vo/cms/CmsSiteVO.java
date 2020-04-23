package com.you.vo.cms;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-03-01 12:09
 */
@Data
@ToString
@ApiModel(value = "CMS站点VO")
public class CmsSiteVO extends BaseVO {

    //站点ID
    @ApiModelProperty("站点id")
    private String siteId;

    //站点名称
    @ApiModelProperty("站点名称")
    private String siteName;

    //站点名称
    @ApiModelProperty("站点URL")
    private String siteDomain;

    //站点端口
    @ApiModelProperty("站点端口")
    private String sitePort;

    //站点访问地址
    @ApiModelProperty("站点访问地址")
    private String siteWebPath;

    //创建时间
    @ApiModelProperty("创建时间")
    private Date siteCreateTime;

    //站点物理路径
    @ApiModelProperty("站点物理路径")
    private String sitePhysicalPath;
}
