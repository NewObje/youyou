package com.you.domain.order.request;

import com.you.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * Created by MichaelLiu on 2019/3/26.
 */
@Data
@ToString
public class CreateOrderRequest extends RequestData {

    String courseId;

}
