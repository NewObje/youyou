package com.you.api.search;

import com.you.dto.video.VideoEsDTO;
import com.you.vo.RestResultVO;
import com.you.vo.video.VideoEsResVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Michael Liu
 * @create 2020-04-08 14:15
 */
@Api(value = "视频ES查询接口", description = "视频查询")
public interface EsSearchControllerApi {
    /**
     * ES查询视频列表信息
     * @return
     */
    @ApiOperation("ES查询视频列表")
    public RestResultVO<VideoEsResVO> listVideos(VideoEsDTO videoEsDTO);
}
