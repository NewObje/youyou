package com.you.cms.web.controller;

import com.you.api.cms.CmsConfigControllerApi;
import com.you.cms.service.CmsConfigService;
import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:29
 */
@RestController
@RequestMapping("cms")
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    CmsConfigService cmsConfigService;

    /**
     * 查询配置信息
     * @param id
     * @return
     */
    @Override
    @RequestMapping(value = "/config/findbyid/{id}", method = RequestMethod.GET)
    public RestResultVO<CmsConfigVO> getModel(@PathVariable("id") String id) {
        CmsConfigVO cmsConfigVO = cmsConfigService.findConfig(id);
        return RestResultVO.ok(cmsConfigVO);
    }
}
