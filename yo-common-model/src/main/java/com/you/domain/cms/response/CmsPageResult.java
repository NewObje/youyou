package com.you.domain.cms.response;

import com.you.domain.cms.CmsPage;
import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by MichaelLiu on 2019/3/31.
 */
@Data
@NoArgsConstructor
public class CmsPageResult extends ResponseResult {
    CmsPage cmsPage;

    public CmsPageResult(ResultCode resultCode, CmsPage cmsPage) {
        super(resultCode);
        this.cmsPage = cmsPage;
    }
}
