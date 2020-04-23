package com.you.domain.cms.ext;

import com.you.domain.cms.CmsTemplateEntity;
import lombok.Data;
import lombok.ToString;

/**
 * @Author: MichaelLiu
 * @Description:
 * @Date:Created in 2019/1/24 10:04.
 * @Modified By:
 */
@Data
@ToString
public class CmsTemplateExt extends CmsTemplateEntity {

    //模版内容
    private String templateValue;

}
