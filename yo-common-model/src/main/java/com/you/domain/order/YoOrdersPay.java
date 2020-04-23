package com.you.domain.order;

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
@Table(name="yo_orders_pay")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class YoOrdersPay implements Serializable {
    private static final long serialVersionUID = -916357210051689789L;
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String id;
    @Column(name = "order_number")
    private String orderNumber;
    @Column(name = "pay_number")
    private String payNumber;
    private String status;

}
