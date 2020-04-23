package com.you.domain.cms.ext;

import com.you.domain.cms.CmsPage;
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
public class CmsPageExt extends CmsPage {
    private String htmlValue;

}
