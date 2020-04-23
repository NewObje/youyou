package com.you.auth.client;

import com.you.vo.RestResultVO;
import com.you.vo.ucenter.YoUserExtVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Michael Liu
 * @create 2020-04-13 15:32
 */
@FeignClient(value = "YO-SERVICE-UCENTER")
public interface UcenterClients {

    /**
     * 调用用户中心查询用户信息
     * @param userName
     * @return
     */
    @RequestMapping(value = "/user/getuserext/{userName}", method = RequestMethod.GET)
    public RestResultVO<YoUserExtVO> findUserExt(@PathVariable("userName") String userName);
}
