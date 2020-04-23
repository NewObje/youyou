package com.you.api.auth;

import com.you.domain.ucenter.response.JwtResult;
import com.you.dto.auth.LoginDTO;
import com.you.vo.RestResultVO;
import com.you.vo.auth.LoginVO;
import com.you.vo.tag.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-03-21 11:36
 */
@Api(value = "用户认证接口", description = "用户认证")
public interface AuthControllerApi {

    /**
     * 用户登录
     * @return
     */
    @ApiOperation("用户登录")
    public RestResultVO<LoginVO> login(LoginDTO loginDTO);

    /**
     * 用户登出
     * @return
     */
    @ApiOperation("用户登出")
    public RestResultVO<String> loginOut();

    /**
     * 查询用户Jwt令牌
     * @return
     */
    @ApiOperation("查询用户Jwt令牌")
    public RestResultVO<String> userJwt();
}
