package com.simbest.boot.wfengine.rabbitmq.model;/**
 * @author Administrator
 * @create 2020/2/18 19:32.
 */

import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *@ClassName RabbitQueueInfo
 *@Description 队列信息
 *@Author Administrator
 *@Date 2020/2/18 19:32
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ApiModel(value = "rabbitmq声明队列信息")
@Table(name = "mq_rabbit_queue_info",  uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "clientType"}))
public class RabbitQueueInfo extends LogicModel {

    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "MQEX")
    private String id;
    /**
     * 队列BeanName
     */
    @Column(nullable = false, length = 100)
    private String beanName;
    /**
     * 队列routeKey
     */
    @Column(nullable = false, length = 100)
    private String routeKey;

    /**
     * 终端类型，send，消息发送者，receive，消息接受者
     */
    @Column(nullable = false, length = 100)
    private String clientType;

    /**
     * 应用code
     */
    @Column(nullable = false, length = 100)
    private String tenantId;
    /**
     * 交换机
     */
    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private RabbitExchangeInfo rabbitExchangeInfo;

    /**
     * 死信转发到队列
     */
    @ManyToOne
    @JoinColumn(name = "ttl_queue_id")
    private RabbitQueueInfo rabbitQueueInfo;
}
