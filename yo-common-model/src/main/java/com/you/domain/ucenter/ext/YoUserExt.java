package com.you.domain.ucenter.ext;

import com.you.domain.ucenter.YoMenu;
import com.you.domain.ucenter.YoUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Created by MichaelLiu on 2019/3/20.
 */
@Data
@ToString
public class YoUserExt extends YoUser {

    //权限信息
    private List<YoMenu> permissions;

    //企业信息
    private String companyId;
}
