package com.you.domain.filesystem.response;

import com.you.domain.filesystem.FileSystemEntity;
import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/3/5.
 */
@Data
@ToString
public class UploadFileResult extends ResponseResult {
    @ApiModelProperty(value = "文件信息", example = "true", required = true)
    FileSystemEntity fileSystem;
    public UploadFileResult(ResultCode resultCode, FileSystemEntity fileSystem) {
        super(resultCode);
        this.fileSystem = fileSystem;
    }

}
