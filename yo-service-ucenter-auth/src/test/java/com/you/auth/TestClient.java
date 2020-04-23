package com.you.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-04-12 17:38
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testClient() {
        //采用客户端负载均衡，从eureka获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose("YO_SERVICE_UCENTER_AUTH");

        URI uri = serviceInstance.getUri();

        String authUrl = uri + "/auth/oauth/token";

        //URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType
        //url就是 申请令牌的url /oauth/token
        //method http的方法类型
        //requestEntity请求内容
        //responseType，将响应的结果生成的类型
        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpBasic = httpBasic("XcWebApp", "XcWebApp");
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization", httpBasic);
        //2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", "itcast");
        body.add("password", "123");
        body.add("grant_type","password");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
        //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables
        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401){
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> responseEntity = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        Map map = responseEntity.getBody();
        System.out.println(map);
    }

    private String httpBasic(String clientId, String secret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String str = clientId + ":" + secret;
        byte[] bytes = str.getBytes();
        byte[] encode = Base64Utils.encode(bytes);
        return "Basic " + new String(encode);
    }
}
