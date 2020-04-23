package com.you.api.cms;

import com.you.domain.cms.request.TempRequestDTO;
import com.you.dto.cms.CmsTempUpdateDTO;
import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsTemplateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:26
 */
@Api(value = "CMS模板管理接口", description = "cms模板管理接口，提供模板的管理、查询接口")
public interface CmsTempControllerApi {

    @ApiOperation("添加模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "站点ID", value = "siteId"),
            @ApiImplicitParam(name = "模板名", value = "templateName"),
            @ApiImplicitParam(name = "模板参数", value = "templateParameter")
    })
    public RestResultVO<String> addTemplate(String siteId, String templateName, String templateParameter, MultipartFile file) throws IOException;

    @ApiOperation("删除模板")
    @ApiImplicitParam(name = "模板ID", value = "tempId")
    public RestResultVO<String> delTemplate(String tempId);

    @ApiOperation("分页查询所有模板")
    @ApiImplicitParam(name = "查询DTO", value = "tempDTO")
    public RestResultVO<Map<String, Object>> listTemplate(TempRequestDTO tempDTO);

    @ApiOperation("查询所有模板信息")
    public RestResultVO<List<CmsTemplateVO>> listTemplates();

    @ApiOperation("修改模板")
    @ApiImplicitParam(name = "修改DTO", value = "tempDTO")
    public RestResultVO<String> updateTemplate(CmsTempUpdateDTO updateDTO);
}
