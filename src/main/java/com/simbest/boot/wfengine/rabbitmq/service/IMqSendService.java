package com.simbest.boot.wfengine.rabbitmq.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;

/**
 * @author Administrator
 * @create 2020/2/18 10:50.
 */
public interface IMqSendService extends ILogicService<MqSend,String> {

    public void sendSms(MqSend mqSend);

}
