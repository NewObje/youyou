package com.you.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-03-01 11:40
 */
@Data
@ToString
public class BadRequestVO extends RestResultVO {

    Map<String, String> fieldErrors;

    String message;


    String errorCode;

    BadRequestVO() {
    }

    BadRequestVO(String message, String errorCode, Map<String, String> fieldErrors) {
        this.errorCode = errorCode;
        this.message = message;
        this.fieldErrors = fieldErrors;
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
