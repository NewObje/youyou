package com.you.domain.order.response;

import com.you.domain.order.YoOrders;
import com.you.model.response.ResponseResult;
import com.you.model.response.ResultCode;
import lombok.Data;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/3/26.
 */
@Data
@ToString
public class CreateOrderResult extends ResponseResult {
    private YoOrders yoOrders;
    public CreateOrderResult(ResultCode resultCode, YoOrders yoOrders) {
        super(resultCode);
        this.yoOrders = yoOrders;
    }


}
