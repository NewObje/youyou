package com.you.cms.web.controller;
import com.you.api.cms.CmsTempControllerApi;
import com.you.cms.service.CmsTempService;
import com.you.domain.cms.request.TempRequestDTO;
import com.you.dto.cms.CmsTempUpdateDTO;
import com.you.vo.RestResultVO;
import com.you.vo.cms.CmsTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2019-05-27-02-21 18:06
 */
@RestController
@RequestMapping("cms")
public class CmsTempController implements CmsTempControllerApi {

    @Autowired
    CmsTempService cmsTempService;

    /**
     * 添加模板
     * @param siteId
     * @param templateName
     * @param templateParameter
     * @param file
     * @return
     */
    @Override
    @RequestMapping(value = "/temp/addtemp", method = RequestMethod.POST)
    public RestResultVO<String> addTemplate(@RequestParam String siteId,
                                            @RequestParam String templateName,
                                            @RequestParam String templateParameter,
                                            @RequestParam MultipartFile file) {
        String str = cmsTempService.addTemplate(siteId, templateName, templateParameter, file);
        return RestResultVO.ok(str, true);
    }

    /**
     * 删除模板
     * @param tempId
     * @return
     */
    @Override
    @RequestMapping(value = "/temp/delete/{tempId}", method = RequestMethod.GET)
    public RestResultVO<String> delTemplate(@PathVariable("tempId") String tempId) {
        String msg = cmsTempService.delTempById(tempId);
        return RestResultVO.ok(msg, true);
    }

    /**
     * 查询所有模板
     * @param tempDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/temp/listtemps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResultVO<Map<String, Object>> listTemplate(@RequestBody TempRequestDTO tempDTO) {
        Map<String, Object> resultMao = cmsTempService.listTemps(tempDTO);
        return RestResultVO.ok(resultMao, true);
    }

    /**
     * 查询所有模板信息
     * @return
     */
    @Override
    @RequestMapping(value = "/temp/list", method = RequestMethod.GET)
    public RestResultVO<List<CmsTemplateVO>> listTemplates() {
        List<CmsTemplateVO> list = cmsTempService.listTemp();
        return RestResultVO.ok(list);
    }

    /**
     * 修改模板
     * @param updateDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/temp/update", method = RequestMethod.POST)
    public RestResultVO<String> updateTemplate(@RequestBody CmsTempUpdateDTO updateDTO) {
        String msg = cmsTempService.updateTemp(updateDTO);
        return RestResultVO.ok(msg, true);
    }


    @RequestMapping(value = "/temp/gettemp/{tempId}", method = RequestMethod.GET)
    public RestResultVO<CmsTemplateVO> getTempById(@PathVariable("tempId") String tempId) {
        /*//根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开下载流对象
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsResource，用于获取流对象
        GridFsResource resource = new GridFsResource(gridFSFile, downloadStream);
        try {
            String str = IOUtils.toString(resource.getInputStream(), "utf-8");
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        CmsTemplateVO templateVO = cmsTempService.findTempById(tempId);
        return RestResultVO.ok(templateVO, true);
    }
}
