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
public class JwtResult extends ResponseResult {
    public JwtResult(ResultCode resultCode, String jwt) {
        super(resultCode);
        this.jwt = jwt;
    }
    private String jwt;
}
