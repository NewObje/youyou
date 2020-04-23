package com.you.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Michael Liu
 * @create 2020-04-12 16:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void redisTest() {
        //定义key
        String key = "user_token:9734b68f-cf5e-456f-9bd6-df578c711390";
        //定义Map
        Map<String,String> mapValue = new HashMap<>();
        mapValue.put("id","101");
        mapValue.put("username","KoBeLiu");
        String value = JSON.toJSONString(mapValue);
        //向redis中存储字符串
        redisTemplate.boundValueOps(key).set(value,60, TimeUnit.SECONDS);
        //读取过期时间，已过期返回-2
        Long expire = redisTemplate.getExpire(key);
        System.out.println(expire);
        //根据key获取value
        String s = redisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
