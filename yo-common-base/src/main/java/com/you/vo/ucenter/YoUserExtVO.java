package com.you.vo.ucenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Michael Liu
 * @create 2020-04-13 14:41
 */
@Data
@ToString
@ApiModel(value = "用户信息VO")
public class YoUserExtVO extends YoUserVO {

    /**
     * 权限信息
     */
    @ApiModelProperty("权限信息列表")
    private List<YoMenuVO> permissions;

    /**
     * 企业信息
     */
    @ApiModelProperty("企业信息")
    private String companyId;
}
