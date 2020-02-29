package com.simbest.boot.wfengine.rabbitmq.register;/**
 * @author Administrator
 * @create 2020/2/19 0:13.
 */

import com.simbest.boot.wfengine.rabbitmq.handler.ReceiveHandler;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitExchangeInfoService;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitQueueInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *@ClassName RegisterExchangeQueue
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/19 0:13
 *@Version 1.0
 **/
@Component
@Slf4j
public class RegisterExchangeQueue {

    @Autowired
    private IRabbitExchangeInfoService rabbitExchangeInfoServiceImpl;

    @Autowired
    private IRabbitQueueInfoService rabbitQueueInfoServiceImpl;

    @Autowired
    private AmExchangeDeclare amExchangeDeclare;

    @Autowired
    private AmQueueDeclare amQueueDeclare;

    @Autowired
    private AmBindDeclare amBindDeclare;

    @PostConstruct
    public void registerExchangeQueue(){
        List<RabbitExchangeInfo> rabbitExchangeInfos = rabbitExchangeInfoServiceImpl.findAllNoPage();

        if(rabbitExchangeInfos!=null && rabbitExchangeInfos.size()>0){
            for(RabbitExchangeInfo rabbitExchangeInfo : rabbitExchangeInfos){
                amExchangeDeclare.declareExchange(rabbitExchangeInfo);
            }
        }

        List<RabbitQueueInfo> rabbitQueueInfos = rabbitQueueInfoServiceImpl.findAllNoPage();
        if(rabbitQueueInfos!=null && rabbitQueueInfos.size()>0){
            for(RabbitQueueInfo rabbitQueueInfo : rabbitQueueInfos){
                amQueueDeclare.declareQueue(rabbitQueueInfo);
                if(rabbitQueueInfo.getRabbitExchangeInfo()!=null){
                    amBindDeclare.queueBind(rabbitQueueInfo.getBeanName(),rabbitQueueInfo.getRabbitExchangeInfo().getExchangeName(),rabbitQueueInfo.getRouteKey());
                }
            }
        }

    }

    @Bean
    public SimpleMessageListenerContainer mqMessageContainer(ConnectionFactory connectionFactory,ReceiveHandler receiveHandler) throws AmqpException, IOException {
        List<RabbitQueueInfo> rabbitQueueInfos = rabbitQueueInfoServiceImpl.findAllNoPage();
        List<String> rabbitQueueInfoStringList = new ArrayList<String>();
        if(rabbitQueueInfos!=null && rabbitQueueInfos.size()>0){
            for(RabbitQueueInfo rabbitQueueInfo : rabbitQueueInfos){
                rabbitQueueInfoStringList.add(rabbitQueueInfo.getBeanName());
            }
            String[] queueNames = new String[rabbitQueueInfoStringList.size()];
            rabbitQueueInfoStringList.toArray(queueNames);

            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
            container.setQueueNames(queueNames);
            container.setExposeListenerChannel(true);
//        container.setPrefetchCount(prefetchCount);//设置每个消费者获取的最大的消息数量
            container.setConcurrentConsumers(1);//消费者个数
            container.setAcknowledgeMode(AcknowledgeMode.MANUAL);//设置确认模式为手工确认
            container.setMessageListener(receiveHandler);//监听处理类
            return container;
        }
        return null;

    }

}
