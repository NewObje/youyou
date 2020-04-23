package com.you.domain.cms.response;

import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;

/**
 * Created by MichaelLiu on 2019/3/31.
 */
@Data
public class GenerateHtmlResult extends ResponseResult {
    String html;
    public GenerateHtmlResult(ResultCode resultCode, String html) {
        super(resultCode);
        this.html = html;
    }
}
