package com.you.domain.tag.response;

import com.google.common.collect.ImmutableMap;
import com.you.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by MichaelLiu on 2019/3/5.
 */
@ToString
public enum TagCode implements ResultCode {

    TAG_IS_EXISTED(false,23001,"视频标签已存在！"),
    TAG_IS_NOT_EXIST(false,23002,"视频标签不存在！"),
    TAG_IS_USED_BY_VIDEO(false,23003,"视频标签已被其他视频引用不能删除！");

    //操作代码
    @ApiModelProperty(value = "媒资系统操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "媒资系统操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "媒资系统操作提示", example = "文件在系统已存在！", required = true)
    String message;
    private TagCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, TagCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, TagCode> builder = ImmutableMap.builder();
        for (TagCode commonCode : values()) {
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
