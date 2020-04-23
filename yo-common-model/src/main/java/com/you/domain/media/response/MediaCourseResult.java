package com.you.domain.media.response;

import com.you.domain.media.MediaFile;
import com.you.domain.media.MediaVideoCourse;
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
public class MediaCourseResult extends ResponseResult {
    public MediaCourseResult(ResultCode resultCode, MediaVideoCourse mediaVideoCourse) {
        super(resultCode);
        this.mediaVideoCourse = mediaVideoCourse;
    }

    MediaFile mediaVideo;
    MediaVideoCourse mediaVideoCourse;
}
