package com.you.auth.web.controller;

import com.you.api.auth.AuthControllerApi;
import com.you.auth.service.AuthService;
import com.you.domain.ucenter.ext.AuthToken;
import com.you.domain.ucenter.response.AuthCode;
import com.you.domain.ucenter.response.JwtResult;
import com.you.dto.auth.LoginDTO;
import com.you.exception.ExceptionCast;
import com.you.model.response.CommonCode;
import com.you.utils.CookieUtil;
import com.you.vo.RestResultVO;
import com.you.vo.auth.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-04-11 15:20
 */
@RestController
public class AuthController implements AuthControllerApi {

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Value("${auth.clientId}")
    String clientId;

    @Value("${auth.clientSecret}")
    String clientSecret;

    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Autowired
    AuthService authService;


    /**
     * 用户登录
     * @param loginDTO
     * @return
     */
    @Override
    @PostMapping(value = "/userlogin")
    public RestResultVO<LoginVO> login(LoginDTO loginDTO) {
        if (null == loginDTO || StringUtils.isEmpty(loginDTO.getUsername())) {
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }

        if (null == loginDTO || StringUtils.isEmpty(loginDTO.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }

        AuthToken authToken = authService.login(loginDTO.getUsername(), loginDTO.getPassword(), clientId, clientSecret);
        //访问token
        String access_token = authToken.getAccess_token();
        //将访问令牌存储到cookie
        this.saveTokenToCookie(access_token);
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(access_token);
        return RestResultVO.ok(loginVO, true);
    }

    /**
     * 将访问令牌存储到cookie
     * @param access_token
     */
    private void saveTokenToCookie(String access_token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //添加cookie 认证令牌，最后一个参数设置为false，表示允许浏览器获取
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", access_token, cookieMaxAge, false);
    }

    /**
     * 用户登出
     * @return
     */
    @Override
    @PostMapping("/userlogout")
    public RestResultVO<String> loginOut() {
        // 取出身份令牌
        String uid = this.getTokenFromCookie();
        // 删除redis中token
        boolean delStatus = authService.logOut(uid);
        // 删除cookie
        this.delCookie(uid);
        return RestResultVO.ok("退出成功！", true);
    }

    /**
     * 获取jwt令牌，包含用户信息
     */
    @Override
    @GetMapping("/userjwt")
    public RestResultVO<String> userJwt() {
        // 从cookie中读取访问令牌
        String access_token = this.getTokenFromCookie();
        AuthToken userToken = authService.getUserToken(access_token);
        if (null == userToken) {
            return RestResultVO.ok("获取失败！", false);
        }
        return RestResultVO.ok(userToken.getJwt_token(), true);
    }

    /**
     * 从cookie中读取访问令牌
     * @return
     */
    private String getTokenFromCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        return cookieMap.get("uid");
    }

    /**
     * 清除cookie
     * @param token
     */
    private void delCookie(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", token, 0, false);
    }
}
