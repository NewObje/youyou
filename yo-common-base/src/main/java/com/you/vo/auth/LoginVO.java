package com.you.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-04-12 16:32
 */
@Data
@ToString
@ApiModel(value = "用户登录VO")
public class LoginVO {

    @ApiModelProperty(value = "主键ID", name = "id")
    private String token;
}
