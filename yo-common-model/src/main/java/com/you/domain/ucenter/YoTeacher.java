package com.you.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by MichaelLiu on 2019/2/10.
 */
@Data
@ToString
@Entity
@Table(name="yo_teacher")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class YoTeacher implements Serializable {
    private static final long serialVersionUID = -916357110051689786L;
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(length = 32)
    private String id;
    private String name;
    private String pic;
    private String intro;
    private String resume;
    @Column(name="user_id")
    private String userId;

}
