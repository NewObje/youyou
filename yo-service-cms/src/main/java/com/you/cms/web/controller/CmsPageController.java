package com.you.cms.web.controller;
import com.you.api.cms.CmsPageControllerApi;
import com.you.cms.service.PageService;
import com.you.domain.cms.CmsPage;
import com.you.domain.cms.request.QueryPageRequest;
import com.you.domain.cms.response.CmsPageResult;
import com.you.model.response.QueryResponseResult;
import com.you.model.response.ResponseResult;
import com.you.vo.RestResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Michael Liu
 * @create 2019-05-27-02-21 18:06
 */
@RestController
@RequestMapping("cms")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    PageService pageService;

    /**
     * 分页条件查询页面
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @Override
    @RequestMapping(value = "/listPages/{pageNum}/{pageSize}")
    public QueryResponseResult listPage(@PathVariable("pageNum") int pageNum,
                                        @PathVariable("pageSize") int pageSize,
                                        QueryPageRequest request) {
        QueryResponseResult result = pageService.listCmsPagesByCondition(pageNum, pageSize, request);
        return result;
    }

    /**
     * 查询页面
     * @param pageId
     * @return
     */
    @Override
    @RequestMapping(value = "/findPage/{pageId}")
    public CmsPage findPageById(@PathVariable(value = "pageId") String pageId) {
        return pageService.findPageById(pageId);
    }

    /**
     * 更新页面
     * @param pageId
     * @param cmsPage
     * @return
     */
    @Override
    @RequestMapping(value = "/updatePage/{pageId}")
    public CmsPageResult updatePage(@PathVariable(value = "pageId") String pageId,
                                    @RequestBody CmsPage cmsPage) {
        return pageService.updatePage(pageId, cmsPage);
    }

    /**
     * 删除页面
     * @param pageId
     * @return
     */
    @Override
    @RequestMapping(value = "/deleteById/{pageId}")
    public ResponseResult deletePageById(@PathVariable(value = "pageId") String pageId) {
        return pageService.deleteById(pageId);
    }

    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    @Override
    @RequestMapping(value = "/insert")
    public CmsPageResult insertPage(@RequestBody CmsPage cmsPage) {
        return pageService.insert(cmsPage);
    }

    /**
     * 页面预览
     * @param pageId
     * @return
     */
    @Override
    @RequestMapping(value = "/view/{pageId}", method = RequestMethod.GET)
    public void pageView(@PathVariable("pageId") String pageId, HttpServletResponse response) {
        String page = pageService.buildPage(pageId);

        if (StringUtils.isNotEmpty(page)) {
            try {
                response.setHeader("Content-type","text/html;charset=utf‐8");
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(page.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发布页面
     * @param pageId
     * @return
     */
    @Override
    @RequestMapping(value = "/post/{pageId}", method = RequestMethod.GET)
    public RestResultVO<String> postPage(@PathVariable("pageId") String pageId) {
        String msg = pageService.postPage(pageId);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 视频页面发布，供视频管理服务调用
     * @param cmsPage
     * @return
     */
    @Override
    @RequestMapping(value = "/videoPost", method = RequestMethod.POST)
    public RestResultVO<String> videoPostPage(@RequestBody CmsPage cmsPage) {
        String videoUrl = pageService.videoPost(cmsPage);
        return RestResultVO.ok(videoUrl, true);
    }
}
