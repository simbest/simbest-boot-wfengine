package com.simbest.boot.wfengine.rabbitmq.register;/**
 * @author Administrator
 * @create 2020/2/18 22:26.
 */

import com.rabbitmq.client.AMQP;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AmQueueDeclare
 * @Description TODO
 * @Author Administrator
 * @Date 2020/2/18 22:26
 * @Version 1.0
 **/
@Component
public class AmQueueDeclare {

    @Autowired
    RabbitAdmin rabbitAdmin;
    /**
     * 声明队列
     * 向rabbitMQ服务器声明一个队列
     *
     * @param rabbitQueueInfo
     * @return
     */
    public Queue declareQueue(RabbitQueueInfo rabbitQueueInfo) {
        Queue queue = new Queue(rabbitQueueInfo.getBeanName());
//        BeanUtils.copyProperties(rabbitQueueInfo, queue);
        return queue;
    }

    /**
     * 清空队列中的消息
     *
     * @param queueName
     * @return 清楚队列中的消息的个数
     */
    public int purgeQueue(String queueName) {
        if (StringUtils.isEmpty(queueName)) {
            throw new RabbitMQException("the parameter queueName couldn't not be null");
        }
        return rabbitAdmin.purgeQueue(queueName);
    }

    /**
     * 判断指定的队列是否存在
     * 1. 如果存在则返回该队列
     * 2. 如果不存在则返回null
     *
     * @param queueName
     * @return true 存在， false 不存在
     */
    public boolean isQueueExist(String queueName) {
        if (StringUtils.isEmpty(queueName)) {
            throw new RabbitMQException("the parameter queueName couldn't not be null");
        }
        String isExist = rabbitAdmin.getRabbitTemplate().execute((channel -> {
            try {
                AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(queueName);
                return declareOk.getQueue();
            } catch (Exception e) {
                throw new RabbitMQException(e.getMessage());
            }
        }));
        return StringUtils.isEmpty(isExist) ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 从rabbitMQ服务器中删除指定的队列
     *
     * @param queueName
     * @return
     */
    public boolean deleteQueue(String queueName) {
        if (StringUtils.isEmpty(queueName)) {
            throw new RabbitMQException("the parameter queueName couldn't not be null");
        }
        return rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 从rabbitMQ服务器中删除指定的队列
     *
     * @param queueName 队列名称
     * @param unused    队列是否在使用，如果设置为true则该队列只能在没有被使用的情况下才能删除
     * @param empty     队列是否为空，如果设置为true则该队列只能在该队列没有消息时才会被删除
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        if (StringUtils.isEmpty(queueName)) {
            throw new RabbitMQException("the parameter queueName couldn't not be null");
        }
        rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

}
