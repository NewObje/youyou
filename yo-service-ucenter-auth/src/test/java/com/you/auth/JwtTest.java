package com.you.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-04-12 15:33
 */
public class JwtTest {

    @Test
    public void testJwt() {
        //证书文件
        String key_location = "you.keystore";
        //密钥库密码
        String key_store_pass = "youkeystore";
        //访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_store_pass.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String key_pass = "youyou";
        //密钥别名
        String alias = "yokey";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, key_pass.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "101");
        tokenMap.put("name", "KobeLiu");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");

        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));
        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println("token = " + token);
    }
}
