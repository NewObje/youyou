package com.you.video.client;

import com.you.domain.cms.CmsPage;
import com.you.domain.cms.response.CmsPageResult;
import com.you.domain.filesystem.FileSystemEntity;
import com.you.vo.RestResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * @author Michael Liu
 * @create 2020-03-21 13:01
 */
@FeignClient(value = "YO-SERVICE-CMS-MANAGE")
public interface CmsClient {

    /**
     * 查询页面
     * @param pageId
     * @return
     */
    @RequestMapping(value = "/cms/findPage/{pageId}")
    public CmsPage findPageById(@PathVariable(value = "pageId") String pageId);

    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    @RequestMapping(value = "/cms/insert", method = RequestMethod.POST)
    public CmsPageResult insertPage(@RequestBody CmsPage cmsPage);

    /**
     * 页面预览
     * @param pageId
     * @return
     */
    @RequestMapping(value = "/cms/view/{pageId}", method = RequestMethod.GET)
    public void pageView(@PathVariable("pageId") String pageId, HttpServletResponse response);

    /**
     * 视频页面发布，供视频管理服务调用
     * @param cmsPage
     * @return
     */
    @RequestMapping(value = "/cms/videoPost", method = RequestMethod.POST)
    public RestResultVO<String> videoPostPage(@RequestBody CmsPage cmsPage);
}
