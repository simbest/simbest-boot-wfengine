package com.simbest.boot.wfengine.rabbitmq.model;/**
 * @author Administrator
 * @create 2020/2/18 10:29.
 */

import com.simbest.boot.base.annotations.EntityIdPrefix;
import com.simbest.boot.base.model.LogicModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 *@ClassName MqReceive
 *@Description 应用接受rabbitmq的消息，先入库，再异步进行处理
 *@Author Administrator
 *@Date 2020/2/18 10:29
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mq_receive")
public class MqReceive extends LogicModel {
    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "MQR") //主键前缀，此为可选项注解
    private String id;

    /**
     * 应用code
     */
    @Column(name = "tenantId", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "action", nullable = false, length = 100)
    private String action;//执行动作

    @Column(name = "mapJson", nullable = true, length = 10000)
    private String mapJson;//发送参数

    @Column(name = "businessKey", nullable = true, length = 100)
    private String businessKey;//业务主键

    @Column(name = "processInstId", nullable = true, length = 100)
    private String processInstId;//流程实例Id

    @Column(name = "processDefKey", nullable = true, length = 100)
    private String processDefKey;//流程定义key

    @Column(name = "taskId", nullable = true, length = 100)
    private String taskId;//任务id

    @Column(name = "taskDefinitionKey", nullable = true, length = 100)
    private String taskDefinitionKey;//任务环节


    @Column(name = "receiveDate", nullable = true)
    private Date receiveDate;//接受时间


}
