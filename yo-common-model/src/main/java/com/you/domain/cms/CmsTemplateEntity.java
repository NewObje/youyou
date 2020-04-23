package com.you.domain.cms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: MichaelLiu
 * @Description:
 * @Date:Created in 2019/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
@Document(collection = "cms_template")
public class CmsTemplateEntity {

    //站点ID
    private String siteId;

    //模版ID
    @Id
    private String templateId;

    //模版名称
    private String templateName;

    //模版参数
    private String templateParameter;

    //模版文件Id
    private String templateFileId;
}
