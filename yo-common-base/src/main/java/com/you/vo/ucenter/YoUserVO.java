package com.you.vo.ucenter;

import com.you.vo.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-04-13 14:35
 */
@Data
@ToString
@ApiModel(value = "用户VO")
public class YoUserVO extends BaseVO {
    /**
     * 用户d
     */
    @ApiModelProperty("用户id")
    private String id;

    /**
     * 用户登录名
     */
    @ApiModelProperty("用户登录名")
    private String username;

    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * salt
     */
    @ApiModelProperty("salt")
    private String salt;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String name;

    /**
     * 用户角色类型
     */
    @ApiModelProperty("用户角色类型")
    private String utype;

    /**
     * 用户生日
     */
    @ApiModelProperty("用户生日")
    private String birthday;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String userpic;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    private String sex;

    /**
     * 用户邮箱
     */
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 用户手机
     */
    @ApiModelProperty("用户手机")
    private String phone;

    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    private String status;

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
