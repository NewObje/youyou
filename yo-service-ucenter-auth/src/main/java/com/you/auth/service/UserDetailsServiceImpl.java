package com.you.auth.service;

import com.you.auth.client.UcenterClients;
import com.you.domain.ucenter.YoMenu;
import com.you.domain.ucenter.ext.YoUserExt;
import com.you.vo.RestResultVO;
import com.you.vo.ucenter.YoMenuVO;
import com.you.vo.ucenter.YoUserExtVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-04-11 15:20
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    UcenterClients ucenterClients;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication == null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails != null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }

        //请求ucenter查询用户
        RestResultVO<YoUserExtVO> resultVO = ucenterClients.findUserExt(username);
        YoUserExtVO userExtVO = resultVO.getData();
        if (null == userExtVO) {
            //返回NULL表示用户不存在，Spring Security会抛出异常
            return null;
        }

        //从数据库查询用户正确的密码，Spring Security会去比对输入密码的正确性
        //取出正确密码（hash值）
        String password = userExtVO.getPassword();
        String user_permission_string = "";

        UserJwt userDetails = new UserJwt(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));
        //用户id
        userDetails.setId(userExtVO.getId());
        //用户名称
        userDetails.setName(userExtVO.getName());
        //用户头像
        userDetails.setUserpic(userExtVO.getUserpic());
        //用户所属企业id
        userDetails.setCompanyId(userExtVO.getCompanyId());

        return userDetails;
        //用户权限，这里暂时使用静态数据，最终会从数据库读取
        //从数据库获取权限
        /*List<YoMenuVO> permissions = userExtVO.getPermissions();
        List<String> user_permission = new ArrayList<>();
        permissions.forEach(item-> user_permission.add(item.getCode()));
//        user_permission.add("course_get_baseinfo");
//        user_permission.add("course_find_pic");
        user_permission_string  = StringUtils.join(user_permission.toArray(), ",");
       *//* UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*//*
//                AuthorityUtils.createAuthorityList("course_get_baseinfo","course_get_list"));
        return userDetails;*/
    }
}
