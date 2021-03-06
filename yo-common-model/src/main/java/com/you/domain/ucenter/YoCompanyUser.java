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
@Table(name="you_company_user")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class YoCompanyUser implements Serializable {

    private static final long serialVersionUID = -916357110051689786L;

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;

    @Column(name="company_id")
    private String companyId;

    @Column(name="user_id")
    private String userId;
}
