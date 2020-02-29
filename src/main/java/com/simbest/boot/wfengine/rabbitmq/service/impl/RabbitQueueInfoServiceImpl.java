package com.simbest.boot.wfengine.rabbitmq.service.impl;/**
 * @author Administrator
 * @create 2020/2/18 20:02.
 */

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;
import com.simbest.boot.wfengine.rabbitmq.repository.RabbitQueueInfoRepository;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitQueueInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@ClassName RabbitQueueInfoServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/18 20:02
 *@Version 1.0
 **/
@Slf4j
@Service(value = "rabbitQueueInfoServiceImpl")
public class RabbitQueueInfoServiceImpl extends LogicService<RabbitQueueInfo,String> implements IRabbitQueueInfoService {
    private RabbitQueueInfoRepository repository;
    @Autowired
    public RabbitQueueInfoServiceImpl(RabbitQueueInfoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public RabbitQueueInfo findByTenantIdAndClientType(String tenantId, String clientType) {
        return repository.findByTenantIdAndClientType(tenantId,clientType);
    }
}
