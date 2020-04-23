package com.you.gateway.service;

import com.you.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Michael Liu
 * @create 2020-04-14 21:44
 */
@Service
public class AuthService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 从头取出jwt令牌
     * @param request
     * @return
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        //取出头信息
        String authorization = request.getHeader("Authorization");
        if (null == authorization) {
            return null;
        }
        if (!authorization.startsWith("Bearer ")) {
            return null;
        }
        //取到jwt令牌
        String jwt = authorization.substring(7);
        return jwt;
    }

    /**
     * 从cookie取出token
     * 查询身份令牌
     * @param request
     * @return
     */
    public String getTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String access_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(access_token)) {
            return null;
        }
        return access_token;
    }

    /**
     * 查询令牌的有效期
     * @param access_token
     * @return
     */
    public long getTokenExpire(String access_token) {
        String user_token = "user_token: " + access_token;
        Long expire = stringRedisTemplate.getExpire(user_token, TimeUnit.SECONDS);
        return expire;
    }
}
