package com.you.domain.cms.request;

import com.you.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Michael Liu
 * @create 2019-05-26 15:41
 */
@Data
public class QueryPageRequest extends RequestData {
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;
    //页面ID
    @ApiModelProperty("页面ID")
    private String pageId;
    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    //别名
    @ApiModelProperty("别名")
    private String pageAliase;
    //模版id
    @ApiModelProperty("模版id")
    private String templateId;
}
