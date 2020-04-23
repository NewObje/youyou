package com.you.exception;

import com.you.model.response.ResultCode;

/**
 * @author Michael Liu
 * @create 2020-02-26 17:16
 */
public class CustomException extends RuntimeException {
    private ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }
}
