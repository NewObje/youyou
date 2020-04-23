package com.you.cms.web.controller;
import com.you.api.cms.CmsSiteControllerApi;
import com.you.cms.service.CmsSiteService;
import com.you.dto.cms.CmsSiteCreateDTO;
import com.you.dto.cms.CmsSiteUpdateDTO;
import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsSiteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2019-05-27-02-21 18:06
 */
@RestController
@RequestMapping("cms")
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    CmsSiteService cmsSiteService;

    /**
     * 查询所有站点
     * @return
     */
    @Override
    @RequestMapping(value = "/site/list", method = RequestMethod.GET)
    public RestResultVO<List<CmsSiteVO>> listSites() {
        List<CmsSiteVO> resultList = cmsSiteService.listSites();
        return RestResultVO.ok(resultList, true);
    }

    /**
     * 查询站点
     * @param siteId
     * @return
     */
    @Override
    @RequestMapping(value = "/site/getbyid/{siteId}", method = RequestMethod.GET)
    public RestResultVO<CmsSiteVO> getSiteById(@PathVariable("siteId") String siteId) {
        CmsSiteVO siteVO = cmsSiteService.getSiteById(siteId);
        return RestResultVO.ok(siteVO, true);
    }

    /**
     * 添加站点
     * @param cmsSiteCreateDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/site/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResultVO<String> addSite(@RequestBody CmsSiteCreateDTO cmsSiteCreateDTO) {
        String str = cmsSiteService.addSite(cmsSiteCreateDTO);
        return RestResultVO.ok(str, true);
    }

    /**
     * 删除站点
     * @param siteId
     * @return
     */
    @Override
    @RequestMapping(value = "/site/delete/{siteId}", method = RequestMethod.GET)
    public RestResultVO<String> delSite(@PathVariable("siteId") String siteId) {
        String str = cmsSiteService.deleteSite(siteId);
        return RestResultVO.ok(str, true);
    }

    /**
     * 更新站点
     * @param cmsSiteUpdateDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/site/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResultVO<String> updateSite(@RequestBody CmsSiteUpdateDTO cmsSiteUpdateDTO) {
        String str = cmsSiteService.updateSite(cmsSiteUpdateDTO);
        return RestResultVO.ok(str, true);
    }
}
