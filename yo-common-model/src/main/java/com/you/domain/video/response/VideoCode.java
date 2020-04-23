package com.you.domain.video.response;

import com.google.common.collect.ImmutableMap;
import com.you.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by MichaelLiu on 2019/3/5.
 */
@ToString
public enum VideoCode implements ResultCode {
    VIDEO_POST_FAILED(false,31001,"视频发布成功！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;

    private VideoCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, VideoCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, VideoCode> builder = ImmutableMap.builder();
        for (VideoCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
