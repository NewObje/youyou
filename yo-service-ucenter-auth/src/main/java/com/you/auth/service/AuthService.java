package com.you.auth.service;

import com.alibaba.fastjson.JSON;
import com.you.domain.ucenter.ext.AuthToken;
import com.you.domain.ucenter.response.AuthCode;
import com.you.exception.ExceptionCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Michael Liu
 * @create 2020-04-12 18:21
 */
@Service
public class AuthService {

    public static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 用户认证
     * @param userName
     * @param passWord
     * @param clientId
     * @param clientSecret
     * @return
     */
    public AuthToken login(String userName, String passWord, String clientId, String clientSecret) {
        //申请token令牌
        AuthToken authToken = this.applyToken(userName, passWord, clientId, clientSecret);
        //将token存储到redis
        String access_token = authToken.getAccess_token();
        String tokenJson = JSON.toJSONString(authToken);
        //redis存储token
        boolean saveToken = this.saveToken(access_token, tokenJson, tokenValiditySeconds);

        if (!saveToken) {
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVE_FAIL);
        }
        return authToken;
    }

    /**
     * redis查询令牌
     * @param token
     * @return
     */
    public AuthToken getUserToken(String token) {
        String userToken = "user_token: " + token;
        String userTokenString = stringRedisTemplate.opsForValue().get(userToken);
        if (null != userTokenString) {
            AuthToken authToken = null;
            try {
                authToken = JSON.parseObject(userTokenString, AuthToken.class);
            } catch (Exception e) {
                LOGGER.error("getUserToken from redis and execute JSON.parseObject error: {}", e.getMessage());
                e.printStackTrace();
            }
            return authToken;
        }
        return null;
    }

    /**
     * 用户退出
     * @param access_token
     * @return
     */
    public boolean logOut(String access_token) {
        String user_token = "user_token: " + access_token;
        stringRedisTemplate.delete(user_token);
        return true;
    }

    /**
     * redis存储token
     * @param access_token
     * @param tokenJson
     * @param ttl
     * @return
     */
    private boolean saveToken(String access_token, String tokenJson, long ttl) {
        //令牌名称
        String tokenName = "user_token: " + access_token;
        //保存到令牌到redis
        stringRedisTemplate.boundValueOps(tokenName).set(tokenJson, ttl, TimeUnit.SECONDS);
        //获取过期时间
        Long expire = stringRedisTemplate.getExpire(tokenName);

        return expire > 0;
    }

    /**
     * 申请token
     * @param userName
     * @param passWord
     * @param clientId
     * @param clientSecret
     * @return
     */
    private AuthToken applyToken(String userName, String passWord, String clientId, String clientSecret) {
        //选择认证服务的地址
        ServiceInstance serviceInstance = loadBalancerClient.choose("YO-SERVICE-UCENTER-AUTH");
        if (null == serviceInstance) {
            LOGGER.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_AUTH_SERVER_NOTFOUND);
        }

        //获取令牌的url
        String path = serviceInstance.getUri().toString() + "/auth/oauth/token";

        //定义body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        //授权方式
        formData.add("grant_type", "password");
        //用户
        formData.add("username", userName);
        //密码
        formData.add("password", passWord);

        //定义头
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", this.httpBasic(clientId, clientSecret));

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (400 != response.getRawStatusCode() && 401 != response.getRawStatusCode()) {
                    super.handleError(response);
                }
            }
        });

        //http请求spring security的申请令牌接口
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(formData, headers);
        ResponseEntity<Map> entity = restTemplate.exchange(path, HttpMethod.POST, httpEntity, Map.class);

        //申请令牌信息
        Map map = entity.getBody();

        //jti是jwt令牌的唯一标识作为用户身份令牌
        if (null == map || null == map.get("access_token") || null == map.get("refresh_token") || null == map.get("jti")){
            //解析spring security返回的错误信息
            if (null != map && null != map.get("error_description")) {
                String error_description = (String) map.get("error_description");
                if(error_description.indexOf("UserDetailsService returned null") >= 0){
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }else if(error_description.indexOf("坏的凭证")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }
            }
            return null;
        }

        AuthToken authToken = new AuthToken();
        //jti，作为用户的身份标识
        authToken.setAccess_token((String) map.get("jti"));
        //访问令牌
        authToken.setJwt_token((String) map.get("access_token"));
        //刷新令牌
        authToken.setRefresh_token((String) map.get("refresh_token"));

        return authToken;
    }

    /**
     * 获取httpbasic认证串
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpBasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String str = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(str.getBytes());
        return "Basic " + new String(encode);
    }
}
