package com.you.api.ucenter;

import com.you.vo.RestResultVO;
import com.you.vo.ucenter.YoUserExtVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author Michael Liu
 * @create 2020-04-13 11:50
 */
@Api(value = "用户中心", description = "用户中心管理")
public interface UcenterControllerApi {

    /**
     * 查询用户信息
     * @return
     */
    @ApiOperation("查询用户信息")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, paramType = "path")
    public RestResultVO<YoUserExtVO> findUserExt(String userName);
}
