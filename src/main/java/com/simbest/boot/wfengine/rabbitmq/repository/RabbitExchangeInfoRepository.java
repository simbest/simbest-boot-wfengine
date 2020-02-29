package com.simbest.boot.wfengine.rabbitmq.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitExchangeInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @create 2020/2/18 19:54.
 */
@Repository
public interface RabbitExchangeInfoRepository extends LogicRepository<RabbitExchangeInfo, String> {
    RabbitExchangeInfo findByTenantId(String tenantId);
}
