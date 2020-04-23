package com.you.domain.log;

import com.alibaba.fastjson.JSON;
import com.you.Common.Constant;
import com.you.utils.JodaTimeUtil;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Michael Liu
 * @create 2020-03-24 17:43
 */
@Data
@ToString
@Entity
@Table(name="msg_log")
@GenericGenerator(name = "jpa-assigned", strategy = "assigned")
public class MsgLogEntity implements Serializable {
    private static final long serialVersionUID = -906357110051689482L;

    /**
     * 消息Id
     */
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(name = "msg_id")
    private String msgId;

    /**
     * 消息内容
     */
    @Column(name = "msg")
    private String msg;

    /**
     * 交换机
     */
    @Column(name = "exchange")
    private String exchange;

    /**
     * 路由Key
     */
    @Column(name = "routing_key")
    private String routingKey;

    /**
     * 消息状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 重试次数
     */
    @Column(name = "try_count")
    private Integer tryCount;

    /**
     * 下一次重试时间
     */
    @Column(name = "next_try_time")
    private Date nextTryTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    public MsgLogEntity(String msgId, Object msg, String exchange, String routingKey) {
        this.msgId = msgId;
        this.msg = JSON.toJSONString(msg);
        this.exchange = exchange;
        this.routingKey = routingKey;

        this.status = Constant.MsgLogStatus.DELIVERING;
        this.tryCount = 0;

        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.nextTryTime = (JodaTimeUtil.plusMinutes(date, 1));
    }

    public MsgLogEntity() {
    }
}
