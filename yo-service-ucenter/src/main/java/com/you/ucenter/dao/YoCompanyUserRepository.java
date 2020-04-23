package com.you.ucenter.dao;

import com.you.domain.ucenter.YoCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Michael Liu
 * @create 2020-04-13 14:57
 */
public interface YoCompanyUserRepository extends JpaRepository<YoCompanyUser, String> {

    /**
     * 根据用户id查询所属企业id
     * @param userId
     * @return
     */
    YoCompanyUser findYoCompanyUserByUserId(String userId);
}
