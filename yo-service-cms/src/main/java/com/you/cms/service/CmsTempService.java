package com.you.cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.you.cms.dao.CmsSiteRepository;
import com.you.cms.dao.CmsTempRepository;
import com.you.domain.cms.CmsSiteEnity;
import com.you.domain.cms.CmsTemplateEntity;
import com.you.domain.cms.request.TempRequestDTO;
import com.you.domain.cms.response.CmsCode;
import com.you.dto.cms.CmsTempUpdateDTO;
import com.you.exception.ExceptionCast;
import com.you.utils.BeanUtils;
import com.you.vo.cms.CmsSiteVO;
import com.you.vo.cms.CmsTemplateVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:31
 */
@Service
public class CmsTempService {

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    CmsTempRepository cmsTempRepository;

    @Autowired
    CmsSiteService cmsSiteService;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 存储模板文件调用文件系统服务URL
     */
    @Value("${yofile.templateurl}")
    String templateRestUrl;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 存储模板文件+模板信息
     * @param siteId
     * @param templateName
     * @param templateParameter
     * @param file
     * @return
     */
    public String addTemplate(String siteId,String templateName, String templateParameter, MultipartFile file) {
        if (file.isEmpty()) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        CmsTemplateEntity templateEntity = new CmsTemplateEntity();
        try {
            //存储模板文件获取文件ID
            String fileId = this.tempStore(file);
            templateEntity.setSiteId(siteId);
            templateEntity.setTemplateName(templateName);
            templateEntity.setTemplateParameter(templateParameter);
            templateEntity.setTemplateFileId(fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        cmsTempRepository.insert(templateEntity);
        return "操作成功！";
    }

    /**
     * 存储模板文件
     * @param file
     * @return
     */
    private String tempStore(MultipartFile file) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()){
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(templateRestUrl + "uploadtemp", httpEntity, String.class);
        //返回模板文件ID
        return entity.getBody();
    }

    /**
     * 分页查询所有模板
     * @param tempDTO
     * @return
     */
    public Map<String, Object> listTemps(TempRequestDTO tempDTO) {
        if (null == tempDTO) {
            tempDTO = new TempRequestDTO();
        }
        if (tempDTO.getPageNum() <= 0 ) {
            tempDTO.setPageNum(1);
        }
        tempDTO.setPageNum(tempDTO.getPageNum() - 1);
        //构建条件匹配器
        Query query = new Query();
        if (StringUtils.isNotEmpty(tempDTO.getSiteId())) {
            query.addCriteria(Criteria.where("siteId").is(tempDTO.getSiteId()));
        }
        if (StringUtils.isNotEmpty(tempDTO.getTemplateName())) {
            query.addCriteria(Criteria.where("templateName").regex(".*?\\" +tempDTO.getTemplateName()+ ".*"));
        }
        PageRequest pageRequest = PageRequest.of(tempDTO.getPageNum(), tempDTO.getPageSize());
        List<CmsTemplateEntity> cmsTemplates = mongoTemplate.find(query.with(pageRequest), CmsTemplateEntity.class);

        List<CmsTemplateVO> resultList = new ArrayList<>();
        cmsTemplates.stream().forEach(cmsTemplateEntity -> {
            resultList.add(BeanUtils.clone(cmsTemplateEntity, CmsTemplateVO.class));
        });

        long count = mongoTemplate.count(query, CmsTemplateEntity.class);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", resultList);
        resultMap.put("total", count);
        return resultMap;
    }

    /**
     * 删除模板
     * @param tempId
     * @return
     */
    public String delTempById(String tempId) {
        Optional<CmsTemplateEntity> optional = cmsTempRepository.findById(tempId);
        if (optional.isPresent()) {
            String fileId = optional.get().getTemplateFileId();
            boolean delStatus = this.delTemp(fileId);
            if (delStatus) {
                cmsTempRepository.deleteById(tempId);
                return "删除成功！";
            }
        }
        return "删除失败！";
    }

    /**
     * 删除模板文件
     * @param fileId
     * @return
     */
    private boolean delTemp(String fileId) {
        ResponseEntity<Boolean> delEntity = restTemplate.getForEntity(templateRestUrl + "delete/" + fileId, boolean.class);
        return delEntity.getBody();
    }

    /**
     * 修改模板
     * @param updateDTO
     * @return
     */
    public String updateTemp(CmsTempUpdateDTO updateDTO) {
        CmsTemplateVO templateVO = this.findTempById(updateDTO.getTemplateId());
        if (null != templateVO) {
            templateVO.setSiteId(updateDTO.getSiteId());
            templateVO.setTemplateName(updateDTO.getTemplateName());
            templateVO.setTemplateParameter(updateDTO.getTemplateParameter());
            CmsTemplateEntity cmsTemplateEntity = BeanUtils.clone(templateVO, CmsTemplateEntity.class);
            cmsTempRepository.save(cmsTemplateEntity);
            return "修改成功！";
        }
        return "模板信息不存在！";
    }

    /**
     * 通过ID查询模板信息
     * @param temId
     * @return
     */
    public CmsTemplateVO findTempById(String temId) {
        Optional<CmsTemplateEntity> optional = cmsTempRepository.findById(temId);
        if (optional.isPresent()) {
            return BeanUtils.clone(optional.get(), CmsTemplateVO.class);
        }
        return null;
    }

    /**
     * 查询所有模板信息
     * @return
     */
    public List<CmsTemplateVO> listTemp() {
        List<CmsTemplateEntity> entities = cmsTempRepository.findAll();
        List<CmsTemplateVO> resultList = new ArrayList<>();
        entities.stream().forEach(cmsTemplateEntity -> {
            resultList.add(BeanUtils.clone(cmsTemplateEntity, CmsTemplateVO.class));
        });
        return resultList;
    }
}
