package com.you.search.service;

import com.you.dto.video.VideoEsDTO;
import com.you.vo.video.VideoEsResVO;
import com.you.vo.video.VideoEsVO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Michael Liu
 * @create 2020-04-08 11:34
 */
@Service
public class VideoSearchService {

    public static final Logger LOGGER = LoggerFactory.getLogger(VideoSearchService.class);

    @Value("${yo-video.es.index}")
    private String es_index;

    @Value("${yo-video.es.type}")
    private String es_type;

    @Value("${yo-video.es.source_fileds}")
    private String es_source_fileds;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    public VideoEsResVO findByQuery(VideoEsDTO videoEsDTO) {
        if (null == videoEsDTO) {
            videoEsDTO = new VideoEsDTO();
        }
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(es_index);
        //设置搜索类型
        searchRequest.types(es_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过虑源字段
        String[] source_field_array = es_source_fileds.split(",");
        searchSourceBuilder.fetchSource(source_field_array, new String[]{});
        //创建布尔查询对象
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //搜索条件
        //根据关键字搜索
        if (StringUtils.isNotEmpty(videoEsDTO.getVideoName())) {
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(videoEsDTO.getVideoName(), "video_name", "video_desc")
                    .minimumShouldMatch("70%")
                    .field("video_name", 10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }
        if (null != videoEsDTO.getTagId()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("tag_id", videoEsDTO.getTagId()));
        }
        //设置boolQueryBuilder到searchSourceBuilder
        searchSourceBuilder.query(boolQueryBuilder);
        //设置分页参数
        Integer pageNum = videoEsDTO.getPageNum();
        Integer pageSize = videoEsDTO.getPageSize();
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 16;
        }
        //起始记录下标
        int from = (pageNum - 1) * pageSize;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(pageSize);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("video_name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        VideoEsResVO videoEsResVO = new VideoEsResVO();
        List<VideoEsVO> list = new ArrayList<>();
        try {
            //执行搜索
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            //获取响应结果
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
            long totalHits = hits.getTotalHits();
            //设置总记录数
            videoEsResVO.setTotal(totalHits);

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                VideoEsVO videoEsVO = new VideoEsVO();
                //源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                String id = (String) sourceAsMap.get("id");
                videoEsVO.setId(id);
                String video_name = (String) sourceAsMap.get("video_name");
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if (null != highlightFields) {
                    HighlightField highlightFieldName = highlightFields.get("video_name");
                    if(null != highlightFieldName) {
                        Text[] fragments = highlightFieldName.fragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text text : fragments) {
                            stringBuffer.append(text);
                        }
                        video_name = stringBuffer.toString();
                    }
                }
                videoEsVO.setVideoName(video_name);
                String pic_id = (String) sourceAsMap.get("pic_id");
                videoEsVO.setPicId(pic_id);
                String video_desc = (String) sourceAsMap.get("video_desc");
                videoEsVO.setVideoDesc(video_desc);
                String video_url = (String) sourceAsMap.get("video_url");
                videoEsVO.setVideoUrl(video_url);
                Integer tag_id = (Integer) sourceAsMap.get("tag_id");
                videoEsVO.setTagId(tag_id);
                String upload_time = (String) sourceAsMap.get("upload_time");
                videoEsVO.setUploadTime(upload_time);
                list.add(videoEsVO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoEsResVO.setList(list);
        return videoEsResVO;
    }
}
