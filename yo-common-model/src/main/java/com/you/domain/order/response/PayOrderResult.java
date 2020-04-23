package com.you.domain.order.response;

import com.you.domain.order.YoOrdersPay;
import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/3/27.
 */
@Data
@ToString
public class PayOrderResult extends ResponseResult {
    public PayOrderResult(ResultCode resultCode) {
        super(resultCode);
    }
    public PayOrderResult(ResultCode resultCode,YoOrdersPay yoOrdersPay) {
        super(resultCode);
        this.yoOrdersPay = yoOrdersPay;
    }
    private YoOrdersPay yoOrdersPay;
    private String orderNumber;

    //当tradeState为NOTPAY（未支付）时显示支付二维码
    private String codeUrl;
    private Float money;


}
