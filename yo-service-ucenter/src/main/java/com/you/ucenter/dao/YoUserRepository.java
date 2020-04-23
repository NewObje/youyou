package com.you.ucenter.dao;

import com.you.domain.ucenter.YoUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Michael Liu
 * @create 2020-04-13 14:55
 */
public interface YoUserRepository extends JpaRepository<YoUser, String> {

    YoUser findYoUserByUsername(String userName);
}
