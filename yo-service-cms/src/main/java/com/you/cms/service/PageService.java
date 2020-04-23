package com.you.cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.sun.xml.internal.bind.v2.TODO;
import com.you.cms.config.RabbitmqConfig;
import com.you.cms.dao.CmsPageRepository;
import com.you.cms.dao.CmsTempRepository;
import com.you.domain.cms.CmsPage;
import com.you.domain.cms.CmsTemplateEntity;
import com.you.domain.cms.request.QueryPageRequest;
import com.you.domain.cms.response.CmsCode;
import com.you.domain.cms.response.CmsPageResult;
import com.you.exception.ExceptionCast;
import com.you.model.response.CommonCode;
import com.you.model.response.QueryResponseResult;
import com.you.model.response.QueryResult;
import com.you.model.response.ResponseResult;
import com.you.vo.cms.CmsSiteVO;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-02-24 10:10
 */
@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsTempRepository cmsTempRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CmsSiteService cmsSiteService;

    /**
     * 条件、分页查询
     * @param pageNum
     * @param pageSize
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult listCmsPagesByCondition(int pageNum, int pageSize, QueryPageRequest queryPageRequest) {
        if (null == queryPageRequest) {
            queryPageRequest = new QueryPageRequest();
        }
        if (pageNum <= 0 ) {
            pageNum = 1;
        }
        pageNum = pageNum - 1;
        if (pageSize <= 0) {
            pageSize = 20;
        }
        //构建条件匹配器
        Query query = new Query();
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            query.addCriteria(Criteria.where("pageAliase").regex(".*?\\" +queryPageRequest.getPageAliase()+ ".*"));
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            query.addCriteria(Criteria.where("siteId").is(queryPageRequest.getSiteId()));
        }

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        long count = mongoTemplate.count(query, CmsPage.class);
        List<CmsPage> cmsPages = mongoTemplate.find(query.with(pageable), CmsPage.class);

        QueryResult<CmsPage> queryResult = new QueryResult<>();
        queryResult.setTotal(count);
        queryResult.setList(cmsPages);
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    /**
     * 通过ID查询页面
     * @param pageId
     * @return
     */
    public CmsPage findPageById(String pageId) {
        Optional<CmsPage> cmsPage = cmsPageRepository.findById(pageId);
        if (cmsPage.isPresent()) {
            return cmsPage.get();
        }
        return null;
    }

    /**
     * 修改页面
     * @param pageId
     * @param cmsPage
     * @return
     */
    public CmsPageResult updatePage(String pageId, CmsPage cmsPage) {
        CmsPage page = this.findPageById(pageId);
        if (null != page) {
            //执行更新
            CmsPage save = cmsPageRepository.save(cmsPage);
            if (null != save) {
                CmsPageResult result = new CmsPageResult(CommonCode.SUCCESS, save);
                return result;
            }
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 删除页面
     * @param pageId
     * @return
     */
    public ResponseResult deleteById(String pageId) {
        CmsPage page = this.findPageById(pageId);
        //TODO 删除物理路径页面

        if (null != page) {
            cmsPageRepository.deleteById(pageId);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    public CmsPageResult insert(CmsPage cmsPage) {
        //查看页面是否存在
        CmsPage page = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(),
                cmsPage.getPageName(),
                cmsPage.getPageWebPath());

        if (null != page) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        CmsPage insertPage = cmsPageRepository.insert(cmsPage);
        CmsPageResult result = new CmsPageResult(CommonCode.SUCCESS, insertPage);
        return result;
    }

    /**
     * 页面预览
     * @param pageId
     * @return
     */
    public String buildPage(String pageId) {
        //获取数据模型
        Map model =  this.getModelByPageId(pageId);
        if (null == model) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //获取页面模板
        String templateContent = getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //执行静态化
        String html = generateHtml(templateContent, model);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
     * 静态化
     * @param templateContent
     * @param model
     * @return
     */
    private String generateHtml(String templateContent, Map model) {
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader loader = new StringTemplateLoader();
            loader.putTemplate("template", templateContent);

            //配置模板加载器
            configuration.setTemplateLoader(loader);
            //获取模板
            Template template = configuration.getTemplate("template");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取页面模板
     * @param pageId
     * @return
     */
    private String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.findPageById(pageId);
        if (null == cmsPage) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXISTED);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //获取模板信息
        Optional<CmsTemplateEntity> optional = cmsTempRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplateEntity templateEntity = optional.get();
            String templateFileId = templateEntity.getTemplateFileId();
            //取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource
            GridFsResource resource = new GridFsResource(gridFSFile, downloadStream);
            try {
                String content = IOUtils.toString(resource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 通过页面ID获取数据模型
     * @param pageId
     * @return
     */
    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = this.findPageById(pageId);
        if (null == cmsPage) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXISTED);
        }
        //获取DataUrl
        String dataUrl = cmsPage.getDataUrl();
        if (null == dataUrl) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //获取模型数据
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        return entity.getBody();
    }

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    public String postPage(String pageId) {
        //执行页面静态化获取html
        String html = this.buildPage(pageId);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //存储HTML文件
        CmsPage cmsPage = savePageHtml(pageId, html);
        //发送消息
        sendPostPageMsg(pageId);
        return "发布成功！";
    }

    /**
     * 发送消息
     * @param pageId
     */
    private void sendPostPageMsg(String pageId) {
        CmsPage cmsPage = this.findPageById(pageId);
        if (null == cmsPage) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXISTED);
        }

        Map<String, String> map = new HashMap<>();
        map.put("pageId", pageId);
        String msg = JSON.toJSONString(map);
        String siteId = cmsPage.getSiteId();
        //将siteId作为RoutingKey
        //发布消息
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, msg);
    }

    /**
     * 存储文件
     * @param pageId
     * @param html
     * @return
     */
    private CmsPage savePageHtml(String pageId, String html) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXISTED);
        }

        CmsPage cmsPage = optional.get();
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotEmpty(htmlFileId)) {
            //如果有，删除原来的文件
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        InputStream inputStream = IOUtils.toInputStream(html);
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        String fileId = objectId.toString();
        //将html文件ID存到CmsPage
        cmsPage.setHtmlFileId(fileId);
        CmsPage page = cmsPageRepository.save(cmsPage);
        return page;
    }

    /**
     * 视频页面发布
     * @param cmsPage
     * @return
     */
    public String videoPost(CmsPage cmsPage) {
        CmsPage videoPage = this.saveVideoPage(cmsPage);
        // 发布视频页面
        this.postPage(cmsPage.getPageId());
        CmsSiteVO cmsSiteVO = cmsSiteService.getSiteById(videoPage.getSiteId());
        String siteIp = cmsSiteVO.getSiteDomain();
        String siteWebPath = cmsSiteVO.getSiteWebPath();
        String pageWebPath = videoPage.getPageWebPath();
        String pageName = videoPage.getPageName();
        String videoUrl = siteIp + siteWebPath + pageWebPath + "/" + pageName;
        return videoUrl;
    }

    /**
     * 保存视频页面信息，已存在则更新
     * @param cmsPage
     * @return
     */
    private CmsPage saveVideoPage(CmsPage cmsPage) {
        CmsPage page = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(),
                cmsPage.getPageName(),
                cmsPage.getPageWebPath());
        if (null != page) {
           return cmsPageRepository.save(cmsPage);
        }
        return cmsPageRepository.insert(cmsPage);
    }
}
