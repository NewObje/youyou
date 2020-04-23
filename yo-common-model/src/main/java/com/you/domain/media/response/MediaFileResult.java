package com.you.domain.media.response;

import com.you.domain.media.MediaFile;
import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by MichaelLiu on 2019/3/31.
 */
@Data
@NoArgsConstructor
public class MediaFileResult extends ResponseResult {
    MediaFile mediaFile;
    public MediaFileResult(ResultCode resultCode, MediaFile mediaFile) {
        super(resultCode);
        this.mediaFile = mediaFile;
    }
}
