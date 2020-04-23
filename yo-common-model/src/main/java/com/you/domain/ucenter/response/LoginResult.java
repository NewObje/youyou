package com.you.domain.ucenter.response;

import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/5/21.
 */
@Data
@ToString
@NoArgsConstructor
public class LoginResult extends ResponseResult {

    public LoginResult(ResultCode resultCode,String token) {
        super(resultCode);
        this.token = token;
    }

    private String token;
}
