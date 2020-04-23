package com.you.cmsclient.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.you.cmsclient.dao.CmsPageRepository;
import com.you.cmsclient.dao.CmsSiteRepository;
import com.you.domain.cms.CmsPage;
import com.you.domain.cms.CmsSiteEnity;
import com.you.domain.cms.response.CmsCode;
import com.you.exception.ExceptionCast;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-03-05 17:36
 */
@Service
public class CmsPageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsTemplate gridFsTemplate;

    /**
     * 将html页面保存到页面物理路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        CmsPage cmsPage = this.getPageById(pageId);
        //查询站点
        CmsSiteEnity cmsSite = this.findSiteById(cmsPage.getSiteId());
        //页面物理路径
        String pagePath = cmsSite.getSitePhysicalPath() + cmsSite.getSiteWebPath() + cmsPage.getPageWebPath() + "/" + cmsPage.getPageName();
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream inputStream = this.getFileById(htmlFileId);

        if (null == inputStream) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        FileOutputStream outputStream = null;
        try {
            //保存到物理路径
            outputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件内容
     * @param htmlFileId
     * @return
     */
    private InputStream getFileById(String htmlFileId) {
        try {
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            GridFsResource resource = new GridFsResource(gridFSFile, downloadStream);
            return resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取页面所属站点
     * @param siteId
     * @return
     */
    private CmsSiteEnity findSiteById(String siteId) {
        Optional<CmsSiteEnity> optional = cmsSiteRepository.findById(siteId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_SITE_NOT_EXISTED);
        }
        return optional.get();
    }

    /**
     * 获取CmsPage
     * @param pageId
     * @return
     */
    private CmsPage getPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXISTED);
        }
        return optional.get();
    }
}
