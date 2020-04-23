package com.you.domain.cms.response;

import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/3/5.
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePreviewResult extends ResponseResult {
    public CoursePreviewResult(ResultCode resultCode, String url) {
        super(resultCode);
        this.url = url;
    }

    String url;
}
