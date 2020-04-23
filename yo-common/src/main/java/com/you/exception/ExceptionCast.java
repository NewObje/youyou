package com.you.exception;

import com.you.model.response.ResultCode;

/**
 * @author Michael Liu
 * @create 2020-02-26 17:19
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
