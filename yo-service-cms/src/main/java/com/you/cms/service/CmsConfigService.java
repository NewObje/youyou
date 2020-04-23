package com.you.cms.service;

import com.you.cms.dao.CmsConfigRepository;
import com.you.domain.cms.CmsConfigEntity;
import com.you.utils.BeanUtils;
import com.you.vo.cms.CmsConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-02-26 21:31
 */
@Service
public class CmsConfigService {

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    /**
     * 获取数据模型
     * @param configId
     * @return
     */
    public CmsConfigVO findConfig(String configId) {
        Optional<CmsConfigEntity> cmsConfigEntity = cmsConfigRepository.findById(configId);
        if (cmsConfigEntity.isPresent()) {
            return BeanUtils.clone(cmsConfigEntity.get(), CmsConfigVO.class);
        }
        return null;
     }
}
