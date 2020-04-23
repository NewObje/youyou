package com.you.domain.cms.request;

import com.you.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Michael Liu
 * @create 2019-05-26 15:41
 */
@Data
public class TempRequestDTO extends RequestData {
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;

    //模版名
    @ApiModelProperty("模版名")
    private String templateName;

    //页码
    @ApiModelProperty("页码")
    private int pageNum = 0;

    //条目数
    @ApiModelProperty("条目数")
    private int pageSize = 15;
}
