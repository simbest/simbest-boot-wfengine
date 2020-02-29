package com.simbest.boot.wfengine.rabbitmq.register;/**
 * @author Administrator
 * @create 2020/2/18 22:11.
 */

import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AmExchangeDeclare
 * @Description TODO
 * @Author Administrator
 * @Date 2020/2/18 22:11
 * @Version 1.0
 **/
@Component
public class AmExchangeDeclare {

    @Autowired
    RabbitAdmin rabbitAdmin;

    /**
     * 向rabbitMQ服务器注册指定的交换机以及交换机的类型
     *
     * @param rabbitExchangeInfo
     * @return
     */
    public Exchange declareExchange(RabbitExchangeInfo rabbitExchangeInfo) {
        Exchange exchange = null;
        exchange = this.initExchange(rabbitExchangeInfo);
        rabbitAdmin.declareExchange(exchange);
        return exchange;
    }

    /**
     * 根据不同类型初始化不同类型的交换机
     *
     * @param rabbitExchangeInfo
     * @return
     */
    private Exchange initExchange(RabbitExchangeInfo rabbitExchangeInfo) {
        RabbitExchangeTypeEnum rabbitExchangeTypeEnum = rabbitExchangeInfo.getRabbitExchangeTypeEnum();
        switch (rabbitExchangeTypeEnum) {
            case NORMAL_QUEUE:
                return new DirectExchange(rabbitExchangeInfo.getExchangeName(), true, false);
            case TOPIC_QUEUE:
                return new TopicExchange(rabbitExchangeInfo.getExchangeName(),true, false);
            case FANOUT_QUEUE:
                return new FanoutExchange(rabbitExchangeInfo.getExchangeName(), true, false);
            default:
                return null;
        }
    }

    /**
     * 从RabbitMQ服务端上删除指定的交换机
     *
     * @param exchangeName
     * @return
     */
    public boolean deleteExchange(String exchangeName) {
        if (StringUtils.isEmpty(exchangeName)) {
            throw new RabbitMQException("the parameter exchangeName couldn't not be null");
        }
        return this.rabbitAdmin.deleteExchange(exchangeName);
    }

}
