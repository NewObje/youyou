package com.you.ucenter.service;

import com.you.domain.ucenter.YoCompanyUser;
import com.you.domain.ucenter.YoUser;
import com.you.ucenter.dao.YoCompanyUserRepository;
import com.you.ucenter.dao.YoUserRepository;
import com.you.utils.BeanUtils;
import com.you.vo.ucenter.YoUserExtVO;
import com.you.vo.ucenter.YoUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Michael Liu
 * @create 2020-04-13 15:04
 */
@Service
public class UcenterService {

    @Autowired
    YoUserRepository yoUserRepository;

    @Autowired
    YoCompanyUserRepository yoCompanyUserRepository;

    /**
     * 通过用户账号查询用户
     * @param userName
     * @return
     */
    private YoUserVO findUserByUserName(String userName) {
        YoUser yoUser = yoUserRepository.findYoUserByUsername(userName);
        return BeanUtils.clone(yoUser, YoUserVO.class);
    }

    /**
     * 通过用户账号查询用户扩展信息
     * @param userName
     * @return
     */
    public YoUserExtVO findUserExtByUserName(String userName) {
        YoUserVO yoUserVO = this.findUserByUserName(userName);
        if (null == yoUserVO) {
            return null;
        }

        //用户扩展信息
        YoUserExtVO userExtVO = BeanUtils.clone(yoUserVO, YoUserExtVO.class);
        String userId = userExtVO.getId();
        //查询用户所属公司
        YoCompanyUser yoCompanyUser = yoCompanyUserRepository.findYoCompanyUserByUserId(userId);
        if (null != yoCompanyUser) {
            userExtVO.setCompanyId(yoCompanyUser.getCompanyId());
        }

        return userExtVO;
    }

}
