package com.you.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/5/21.
 */
@Data
@ToString
@NoArgsConstructor
public class UserToken{
    String userId;//用户id
    String utype;//用户类型
    String companyId;//用户所属企业信息
}
