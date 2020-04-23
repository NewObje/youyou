package com.you.cmsclient.mq;

import com.alibaba.fastjson.JSON;
import com.you.cmsclient.dao.CmsPageRepository;
import com.you.cmsclient.service.CmsPageService;
import com.you.domain.cms.CmsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Michael Liu
 * @create 2020-03-05 18:17
 */
@Component
public class ConsumerPostPage {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsPageService cmsPageService;


    @RabbitListener(queues = {"${yo.mq.queue}"})
    public void postPage(String msg) {
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        LOGGER.info("Receive cms post page : {}", msg.toString());
        String pageId = (String) map.get("pageId");
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            LOGGER.error("Receive cms post page, cmsPage is null : {}", msg.toString());
            return;
        }

        //将页面保存到物理路径
        cmsPageService.savePageToServerPath(pageId);
    }


}
