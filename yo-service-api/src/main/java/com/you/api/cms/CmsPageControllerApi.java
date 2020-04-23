package com.you.api.cms;

import com.you.domain.cms.CmsPage;
import com.you.domain.cms.request.QueryPageRequest;
import com.you.domain.cms.response.CmsPageResult;
import com.you.model.response.QueryResponseResult;
import com.you.model.response.ResponseResult;
import com.you.vo.RestResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Michael Liu
 * @create 2019-05-26 15:55
 */
@Api(value = "Cms页面接口管理", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    @ApiOperation("分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "查询条数", required = true, paramType = "path", dataType = "int")
    })
    public QueryResponseResult listPage(int pageNum, int pageSize, QueryPageRequest request);

    @ApiOperation("通过ID查询页面")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    public CmsPage findPageById(String pageId);

    @ApiOperation("修改页面")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    public CmsPageResult updatePage(String pageId, CmsPage cmsPage);

    @ApiOperation("删除页面")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    public ResponseResult deletePageById(String pageId);

    @ApiOperation("新增页面")
    public CmsPageResult insertPage(CmsPage cmsPage);

    @ApiOperation("页面预览")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    public void pageView(String pageId, HttpServletResponse response);

    @ApiOperation("页面发布")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    public RestResultVO<String> postPage(String pageId);

    @ApiOperation("页面发布供视频管理服务调用")
    @ApiImplicitParam(name = "pageId", value = "页面ID", required = true, paramType = "path", dataType = "String")
    public RestResultVO<String> videoPostPage(CmsPage cmsPage);
}

