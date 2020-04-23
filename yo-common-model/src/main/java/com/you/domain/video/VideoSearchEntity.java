package com.you.domain.video;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-04-04 11:05
 */
@Data
@ToString
@Entity
@Document(indexName = "you_video", type = "doc")
public class VideoSearchEntity implements Serializable {

    private static final long serialVersionUID = -9063571100516894991L;

    /**
     * 视频ID
     */
    @Id
    private String id;

    /**
     * 视频标签ID
     */
    private Integer tagId;

    /**
     * 视频名称
     */
    private String videoName;

    /**
     * 视频描述
     */
    private String videoDesc;

    /**
     * 时间戳logstash使用
     */
    private Date timestamp;

    /**
     * 视频发布状态
     */
    private Integer status;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 视频封面ID
     */
    private String picId;

    /**
     * 上传时间
     */
    private String uploadTime;
}
