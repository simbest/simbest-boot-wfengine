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
 *@ClassName MqSend
 *@Description 应用发送消息给rabbitmq，先入库，再异步调用发送
 *@Author Administrator
 *@Date 2020/2/18 10:29
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mq_send")
public class MqSend extends LogicModel {
    @Id
    @Column(name = "id", length = 40)
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.simbest.boot.util.distribution.id.SnowflakeId")
    @EntityIdPrefix(prefix = "MQS") //主键前缀，此为可选项注解
    private String id;

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

    @Column(name = "action", nullable = false, length = 100)
    private String action;//执行动作

    @Column(name = "tenantId", nullable = false, length = 100)
    private String tenantId;

    @Column(name = "mapJson", nullable = true, length = 10000)
    private String mapJson;//发送参数

    @Column(name = "isSend", nullable = false, columnDefinition = "int default 0")
    private Integer isSend;//是否发送成功

    @Column(name = "sendDate", nullable = true)
    private Date sendDate;//发送时间

    @Column(name = "isSuccess", nullable = false, columnDefinition = "int default 0")
    private Integer isSuccess;//是否执行成功

    @Column(name = "receiveDate", nullable = true)
    private Date receiveDate;//收到回复时间


}
