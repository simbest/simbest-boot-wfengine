package com.simbest.boot.wfengine.rabbitmq.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;

/**
 * @author Administrator
 * @create 2020/2/18 20:00.
 */
public interface IRabbitExchangeInfoService  extends ILogicService<RabbitExchangeInfo,String> {
    RabbitExchangeInfo findByTenantId(String tenantId);
}
