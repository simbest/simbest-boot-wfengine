package com.simbest.boot.wfengine.rabbitmq.service.impl;/**ator
 *@Date 2020/2/18 20:02
 *@Version 1.0
 * @author Administrator
 * @create 2020/2/18 20:02.
 */

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;
import com.simbest.boot.wfengine.rabbitmq.repository.RabbitExchangeInfoRepository;
import com.simbest.boot.wfengine.rabbitmq.service.IRabbitExchangeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *@ClassName RabbitExchangeInfoServiceImpl
 *@Description TODO
 *@Author Administr
 **/
@Slf4j
@Service(value = "rabbitExchangeInfoServiceImpl")
public class RabbitExchangeInfoServiceImpl extends LogicService<RabbitExchangeInfo,String> implements IRabbitExchangeInfoService {
    private RabbitExchangeInfoRepository repository;
    @Autowired
    public RabbitExchangeInfoServiceImpl(RabbitExchangeInfoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public RabbitExchangeInfo findByTenantId(String tenantId) {
        return repository.findByTenantId(tenantId);
    }
}
