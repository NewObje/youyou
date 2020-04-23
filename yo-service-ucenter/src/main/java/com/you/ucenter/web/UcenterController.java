package com.you.ucenter.web;

import com.you.api.ucenter.UcenterControllerApi;
import com.you.ucenter.service.UcenterService;
import com.you.vo.RestResultVO;
import com.you.vo.ucenter.YoUserExtVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Liu
 * @create 2020-04-13 11:45
 */
@RestController
@RequestMapping("user")
public class UcenterController implements UcenterControllerApi {

    @Autowired
    UcenterService ucenterService;

    @Override
    @RequestMapping(value = "/getuserext/{userName}", method = RequestMethod.GET)
    public RestResultVO<YoUserExtVO> findUserExt(@PathVariable("userName") String userName) {
        YoUserExtVO yoUserExtVO = ucenterService.findUserExtByUserName(userName);
        return RestResultVO.ok(yoUserExtVO, true);
    }
}
