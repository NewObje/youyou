package com.you.search.web;

import com.you.api.search.EsSearchControllerApi;
import com.you.dto.video.VideoEsDTO;
import com.you.search.service.VideoSearchService;
import com.you.vo.RestResultVO;
import com.you.vo.video.VideoEsResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael Liu
 * @create 2020-04-08 11:35
 */
@RestController
@RequestMapping("search")
public class VideoSearchController implements EsSearchControllerApi {

    @Autowired
    VideoSearchService videoSearchService;

    /**
     * ES视频查询
     * @param videoEsDTO
     * @return
     */
    @Override
    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public RestResultVO<VideoEsResVO> listVideos(@RequestBody VideoEsDTO videoEsDTO) {
        VideoEsResVO videoEsResVO = videoSearchService.findByQuery(videoEsDTO);
        return RestResultVO.ok(videoEsResVO);
    }
}
