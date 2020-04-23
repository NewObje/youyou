package com.you.domain.ucenter.request;

import com.you.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/3/5.
 */
@Data
@ToString
public class LoginRequestEntity extends RequestData {

    String username;

    String password;

    String verifycode;

}
