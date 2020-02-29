package com.simbest.boot.wfengine.rabbitmq.register;/**
 * @author Administrator
 * @create 2020/2/18 22:27.
 */

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName AmBindDeclare
 * @Description TODO
 * @Author Administrator
 * @Date 2020/2/18 22:27
 * @Version 1.0
 **/
@Component
public class AmBindDeclare {

    @Autowired
    RabbitAdmin rabbitAdmin;

    /**
     * 队列与交换机进行绑定
     *
     * @param queueName    队列名称
     * @param exchangeName 交换机名称
     * @param routingKey   路由键
     * @return
     */
    public boolean queueBind(String queueName, String exchangeName, String routingKey) {
        return this.bing(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);
    }

    /**
     * 交换机和交换机进行绑定
     *
     * @param destExchangeName 目标交换机名称
     * @param exchangeName     交换机名称
     * @param routingKey       路由键
     * @return
     */
    public boolean exchangeBind(String destExchangeName, String exchangeName, String routingKey) {
        return this.bing(destExchangeName, Binding.DestinationType.EXCHANGE, exchangeName, routingKey, null);
    }

    /**
     * bind绑定
     *
     * @param destName     目标名称（可以是队列 也可以是交换机）
     * @param type         绑定的类型 交换机 / 队列
     * @param exchangeName 交换机的名称
     * @param routingKey   路由键
     * @param map          结构参数
     * @return
     */
    private boolean bing(String destName, Binding.DestinationType type, String exchangeName, String routingKey, Map<String, Object> map) {
        Binding binding = new Binding(destName, Binding.DestinationType.QUEUE, exchangeName, routingKey, map);
        try {
            this.rabbitAdmin.declareBinding(binding);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


}
