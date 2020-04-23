package com.you.domain.video;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-03-22 14:12
 */
@Data
@ToString
@Entity
@Table(name="you_video")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class VideoDetailEntity implements Serializable {

    private static final long serialVersionUID = -906357110051689480L;
    /**
     * 视频ID
     */
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(name = "id")
    private String id;

    /**
     * 视频标签ID
     */
    @Column(name = "tag_id")
    private Integer tagId;

    /**
     * 视频名称
     */
    @Column(name = "video_name")
    private String videoName;

    /**
     * 视频描述
     */
    @Column(name = "video_desc")
    private String videoDesc;

    /**
     * 上传时间
     */
    @Column(name = "upload_time")
    private Date uploadTime;

    /**
     * 视频发布状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 视频url
     */
    @Column(name = "video_url")
    private String videoUrl;
}
