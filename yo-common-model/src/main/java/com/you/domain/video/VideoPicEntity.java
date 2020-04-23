package com.you.domain.video;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Michael Liu
 * @create 2020-03-22 14:12
 */
@Data
@ToString
@Entity
@Table(name="video_pic")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class VideoPicEntity implements Serializable {

    /**
     * 视频ID
     */
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(name = "video_id")
    private String videoId;

    /**
     * 视频封面ID
     */
    @Column(name = "pic_id")
    private String picId;
}
