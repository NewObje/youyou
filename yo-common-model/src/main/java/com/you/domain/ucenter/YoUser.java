package com.you.domain.ucenter;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by MichaelLiu on 2019/3/19.
 */
@Data
@ToString
@Entity
@Table(name="you_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class YoUser {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    private String username;

    private String password;

    private String salt;

    private String name;

    private String utype;

    private String birthday;

    private String userpic;

    private String sex;

    private String email;

    private String phone;

    private String status;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="update_time")
    private Date updateTime;
}
