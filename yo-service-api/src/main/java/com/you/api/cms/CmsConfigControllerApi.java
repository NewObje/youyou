package com.you.api.cms;

import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:26
 */
@Api(value = "CMS配置管理接口", description = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation("根据ID查询CMS配置数据模型")
    @ApiImplicitParam(name = "id", value = "配置ID", required = true, paramType = "path", dataType = "int")
    public RestResultVO<CmsConfigVO> getModel(String id);
}
