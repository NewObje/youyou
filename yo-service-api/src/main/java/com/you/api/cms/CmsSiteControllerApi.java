package com.you.api.cms;

import com.you.dto.cms.CmsSiteCreateDTO;
import com.you.dto.cms.CmsSiteUpdateDTO;
import com.you.model.response.QueryResponseResult;
import com.you.model.response.ResponseResult;
import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsSiteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:26
 */
@Api(value = "CMS站点管理接口", description = "cms站点管理接口，提供站点的管理、查询接口")
public interface CmsSiteControllerApi {

    @ApiOperation("查询所有站点")
    public RestResultVO<List<CmsSiteVO>> listSites();

    @ApiOperation("通过ID查询站点")
    @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, paramType = "path", dataType = "string")
    public RestResultVO<CmsSiteVO> getSiteById(String siteId);

    @ApiOperation("添加站点")
    public RestResultVO<String> addSite(CmsSiteCreateDTO cmsSiteCreateDTO);

    @ApiOperation("删除站点")
    @ApiImplicitParam(name = "siteId", value = "站点ID", required = true, paramType = "path", dataType = "string")
    public RestResultVO<String> delSite(String siteId);

    @ApiOperation("修改站点")
    public RestResultVO<String> updateSite(CmsSiteUpdateDTO cmsSiteUpdateDTO);
}
