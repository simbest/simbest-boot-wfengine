package com.simbest.boot.wfengine.rabbitmq.register;/**
 * @author Administrator
 * @create 2020/2/19 0:02.
 */

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *@ClassName RabbitMqConfig
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/19 0:02
 *@Version 1.0
 **/
@Configuration
public class RabbitMqConfig {
    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
