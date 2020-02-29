package com.simbest.boot.wfengine.rabbitmq.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;

/**
 * @author Administrator
 * @create 2020/2/19 10:30.
 */
public interface IMqReceiveService  extends ILogicService<MqReceive,String> {
    /**
     *
     * @param message
     * @return 处理是否成功
     */
    public boolean handler(String message);
}
