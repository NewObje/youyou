package com.you.vo.ucenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Michael Liu
 * @create 2020-04-13 14:52
 */
@Data
@ToString
@ApiModel(value = "企业用户关联VO")
public class YoCompanyUserVO {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private String companyId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
}
