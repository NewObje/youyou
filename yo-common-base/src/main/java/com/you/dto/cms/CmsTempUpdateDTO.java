package com.you.dto.cms;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Michael Liu
 * @create 2020-03-02 9:40
 */
@Data
@ToString
@ApiModel(value = "修改模板DTO")
public class CmsTempUpdateDTO extends BaseVO {

    //模板ID
    @ApiModelProperty("模板ID")
    private String templateId;

    //站点ID
    @ApiModelProperty("站点ID")
    private String siteId;

    //模版名称
    @ApiModelProperty("模版名称")
    private String templateName;

    //模版参数
    @ApiModelProperty("模版参数")
    private String templateParameter;

    //模版文件Id
    @ApiModelProperty("模版文件Id")
    private String templateFileId;
}
