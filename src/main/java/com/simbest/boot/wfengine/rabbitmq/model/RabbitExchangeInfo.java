package com.simbest.boot.wfengine.rabbitmq.model;/**
 * @author Administrator
 * @create 2020/2/18 19:06.
 */

import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 *@ClassName RabbitExchangeInfo
 *@Description 交换机信息
 *@Author Administrator
 *@Date 2020/2/18 19:06
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ApiModel(value = "rabbitmq声明交换机信息")
@Table(name = "mq_rabbit_exchang_info")
public class RabbitExchangeInfo  extends LogicModel {
    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "MQEX")
    private String id;

    /**
     * 交换机beanName
     */
    @Column(nullable = false, length = 100,unique = true)
    private String beanName;
    /**
     * 交换机key，没有特殊用途beanName和exchangeName一致
     */
    @Column(nullable = false, length = 100,unique = true)
    private String exchangeName;

    /**
     * 应用code
     */
    @Column(nullable = false, length = 100,unique = true)
    private String tenantId;
    /**
     * 交换机类型
     */
    private RabbitExchangeTypeEnum rabbitExchangeTypeEnum;
}
