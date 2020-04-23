package com.you.vo.ucenter;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-04-13 14:43
 */
@Data
@ToString
@ApiModel(value = "菜单VO")
public class YoMenuVO extends BaseVO {

    /**
     * 菜单id
     */
    @ApiModelProperty("菜单id")
    private String id;

    /**
     * 菜单编码
     */
    @ApiModelProperty("菜单编码")
    private String code;

    /**
     * 父菜单编码
     */
    @ApiModelProperty("父菜单编码")
    private String pCode;

    /**
     * 父id
     */
    @ApiModelProperty("父id")
    private String pId;

    /**
     * 菜单名
     */
    @ApiModelProperty("菜单名")
    private String menuName;

    /**
     * 请求地址
     */
    @ApiModelProperty("请求地址")
    private String url;

    /**
     * 是否是菜单
     */
    @ApiModelProperty("是否是菜单")
    private String isMenu;

    /**
     * 菜单层级
     */
    @ApiModelProperty("菜单层级")
    private Integer level;

    /**
     * 菜单排序
     */
    @ApiModelProperty("菜单排序")
    private Integer sort;

    /**
     * 菜单状态
     */
    @ApiModelProperty("菜单状态")
    private String status;

    /**
     * 菜单icon
     */
    @ApiModelProperty("菜单icon")
    private String icon;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
