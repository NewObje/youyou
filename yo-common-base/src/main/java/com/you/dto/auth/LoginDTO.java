package com.you.dto.auth;

import com.you.dto.RequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-04-12 16:28
 */
@Data
@ToString
@ApiModel(value = "用户登录DTO")
public class LoginDTO extends RequestDTO {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty("验证码")
    private String verifycode;
}
