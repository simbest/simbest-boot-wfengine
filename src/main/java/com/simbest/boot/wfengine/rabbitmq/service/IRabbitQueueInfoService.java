package com.simbest.boot.wfengine.rabbitmq.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;

/**
 * @author Administrator
 * @create 2020/2/18 20:01.
 */
public interface IRabbitQueueInfoService extends ILogicService<RabbitQueueInfo,String> {
    RabbitQueueInfo findByTenantIdAndClientType(String tenantId,String clientType);
}
