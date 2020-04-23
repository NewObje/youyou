package com.you.domain.tag;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Michael Liu
 * @create 2020-03-20 0:01
 */
@Data
@ToString
@Entity
@Table(name="you_classi")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class TagEntity implements Serializable {

    private static final long serialVersionUID = -906357110051689484L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 5)
    private Integer id;

    /**
     * 标签名
     */
    @Column(name = "classi_fication_name")
    private String tagName;
}
