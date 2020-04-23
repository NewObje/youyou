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
public class AuthToken {

    /**
     * 访问token
     */
    String access_token;

    /**
     * 刷新token
     */
    String refresh_token;

    /**
     * jwt令牌
     */
    String jwt_token;
}
