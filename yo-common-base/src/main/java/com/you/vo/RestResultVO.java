package com.you.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-03-01 11:36
 */
@ApiModel(value = "响应对象")
@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // 当类中字段为null时，转为JSON格式时忽略
public class RestResultVO<T> extends BaseVO {

    /** 返回数据 */
    @ApiModelProperty(value = "响应数据", name = "data")
    T data;

    /** 响应状态值 */
    @ApiModelProperty(value = "响应码", name = "httpStatus", required = true, example = "OK")
    HttpStatus httpStatus;

    /** 成功状态值 */
    @ApiModelProperty(value = "成功状态值", name = "success", required = true, example = "true")
    boolean success;

    /** 详细提示信息 */
    @ApiModelProperty(value = "响应消息", name = "detail", example = "操作成功")
    String detail;

    /**
     * 业务成功时调用方法
     * @param data 返回数据
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> ok(X data) {
        RestResultVO<X> result = new RestResultVO<X>();
        result.setHttpStatus(HttpStatus.OK); // 200
        result.setData(data);
        return result;
    }

    /**
     * 业务成功时调用方法
     * @param successMsg 成功提示信息
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> okMsg(String successMsg) {
        RestResultVO<X> result = new RestResultVO<X>();
        result.setHttpStatus(HttpStatus.OK); // 200
        result.setDetail(successMsg);
        return result;
    }

    /**
     * 业务成功时调用方法
     * @param data 返回数据
     * @param successMsg 成功提示信息
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> ok(X data, String successMsg) {
        RestResultVO<X> result = ok(data);
        result.setDetail(successMsg);
        return result;
    }

    /**
     * 业务成功时调用方法
     * @param data 返回数据
     * @param success 成功状态
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> ok(X data, boolean success) {
        RestResultVO<X> result = ok(data);
        result.setSuccess(success);
        return result;
    }

    /**
     * 业务异常时调用方法
     * @param data 返回数据
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> fail(X data) {
        RestResultVO<X> result = new RestResultVO<X>();
        result.setHttpStatus(HttpStatus.BAD_REQUEST); // 400
        result.setData(data);
        return result;
    }

    /**
     * 业务异常时调用方法
     * @param failMsg 错误提示信息
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> failMsg(String failMsg) {
        RestResultVO<X> result = new RestResultVO<X>();
        result.setHttpStatus(HttpStatus.BAD_REQUEST); // 400
        result.setDetail(failMsg);
        return result;
    }

    /**
     * 业务异常时调用方法
     * @param data 返回数据
     * @param failMsg 错误提示信息
     * @return RestResultVO
     */
    public static <X> RestResultVO<X> fail(X data, String failMsg) {
        RestResultVO<X> result = fail(data);
        result.setDetail(failMsg);
        return result;
    }

    public static RestResultVO badRequestData(String message, String errorCode, Map<String, String> fieldErrors) {
        BadRequestVO badRequestData = new BadRequestVO(message, errorCode, fieldErrors);
        return badRequestData;
    }
}
