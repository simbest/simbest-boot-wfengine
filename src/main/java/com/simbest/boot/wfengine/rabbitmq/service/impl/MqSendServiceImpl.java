package com.simbest.boot.wfengine.rabbitmq.service.impl;/**
 * @author Administrator
 * @create 2020/2/18 10:51.
 */

import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;
import com.simbest.boot.wfengine.rabbitmq.repository.MqSendRepository;
import com.simbest.boot.wfengine.rabbitmq.service.IMqSendService;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitExchangeInfoService;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitQueueInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.simbest.boot.config.MultiThreadConfiguration.MULTI_THREAD_BEAN;

/**
 *@ClassName MqSendServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/18 10:51
 *@Version 1.0
 **/
@Slf4j
@Service(value = "mqSendServiceImpl")
public class MqSendServiceImpl extends LogicService<MqSend,String> implements IMqSendService {

    private MqSendRepository mqSendRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    IRabbitExchangeInfoService rabbitExchangeInfoServiceImpl;

    @Autowired
    IRabbitQueueInfoService rabbitQueueInfoServiceImpl;


    @Autowired
    public MqSendServiceImpl(MqSendRepository repository) {
        super(repository);
        this.mqSendRepository = repository;
    }

    @Override
    @Async(MULTI_THREAD_BEAN)
    public void sendSms(MqSend mqSend){
        try {
            RabbitExchangeInfo rabbitExchangeInfo = rabbitExchangeInfoServiceImpl.findByTenantId(mqSend.getTenantId());
            RabbitQueueInfo rabbitQueueInfo = rabbitQueueInfoServiceImpl.findByTenantIdAndClientType(mqSend.getTenantId(),"send");
            rabbitTemplate.convertAndSend(rabbitExchangeInfo.getExchangeName(),rabbitQueueInfo.getRouteKey(),JacksonUtils.obj2json(mqSend));
            mqSend.setIsSend(1);
            mqSend.setSendDate(DateUtil.getCurrent());
            update(mqSend);
            log.info("消息发送成功："+ mqSend.getId());
        }catch (AmqpException e){
            log.error("------------------error消息发送失败:"+mqSend.getId());
            log.error(Exceptions.getStackTraceAsString(e));
        }

    }
}
