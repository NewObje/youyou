package com.you.cms.service;

import com.you.cms.dao.CmsSiteRepository;
import com.you.domain.cms.CmsSiteEnity;
import com.you.domain.cms.response.CmsCode;
import com.you.dto.cms.CmsSiteCreateDTO;
import com.you.dto.cms.CmsSiteUpdateDTO;
import com.you.exception.ExceptionCast;
import com.you.utils.BeanUtils;
import com.you.vo.cms.CmsSiteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:31
 */
@Service
public class CmsSiteService {

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    /**
     * 查询所有站点
     * @return
     */
    public List<CmsSiteVO> listSites() {
        List<CmsSiteEnity> siteEnities = cmsSiteRepository.findAll();
        List<CmsSiteVO> cmsSiteVOS = new ArrayList<>();
        siteEnities.stream().forEach(cmsSiteEnity -> {
            cmsSiteVOS.add(BeanUtils.clone(cmsSiteEnity, CmsSiteVO.class));
        });
        return cmsSiteVOS;
    }

    /**
     * 通过ID查询站点
     * @param siteId
     * @return
     */
    public CmsSiteVO getSiteById(String siteId) {
        Optional<CmsSiteEnity> optionalCmsSiteEnity = cmsSiteRepository.findById(siteId);
        if (optionalCmsSiteEnity.isPresent()) {
            return BeanUtils.clone(optionalCmsSiteEnity.get(), CmsSiteVO.class);
        }
        return null;
    }

    /**
     * 添加站点
     * @param cmsSiteCreateDTO
     * @return
     */
    public String addSite(CmsSiteCreateDTO cmsSiteCreateDTO) {
        if (null == cmsSiteCreateDTO) {
            return null;
        }
        CmsSiteEnity siteEnity = cmsSiteRepository.findBySiteNameAndSiteDomainAndSitePort(cmsSiteCreateDTO.getSiteName(),
                cmsSiteCreateDTO.getSiteDomain(),
                cmsSiteCreateDTO.getSitePort());
        if (null != siteEnity) {
            ExceptionCast.cast(CmsCode.CMS_SITE_EXISTED);
        }
        CmsSiteEnity cmsSiteEnity = BeanUtils.clone(cmsSiteCreateDTO, CmsSiteEnity.class);
        cmsSiteRepository.insert(cmsSiteEnity);
        return "操作成功！";
    }

    /**
     * 删除站点
     * @param siteId
     * @return
     */
    public String deleteSite(String siteId) {
        CmsSiteVO siteVO = this.getSiteById(siteId);
        if (null != siteVO) {
            cmsSiteRepository.deleteById(siteId);
            return "操作成功！";
        }
        return "操作失败！";
    }

    /**
     *更新站点
     * @param cmsSiteUpdateDTO
     * @return
     */
    public String updateSite(CmsSiteUpdateDTO cmsSiteUpdateDTO) {
        CmsSiteVO cmsSiteVO = this.getSiteById(cmsSiteUpdateDTO.getSiteId());
        if (null == cmsSiteVO) {
            ExceptionCast.cast(CmsCode.CMS_SITE_NOT_EXISTED);
        }
        CmsSiteEnity cmsSiteEnity = BeanUtils.clone(cmsSiteUpdateDTO, CmsSiteEnity.class);
        cmsSiteRepository.save(cmsSiteEnity);
        return "操作成功！";
    }
}
