package com.simbest.boot.wfengine.rabbitmq.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.wfengine.rabbitmq.model.RabbitQueueInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @create 2020/2/18 19:55.
 */
@Repository
public interface RabbitQueueInfoRepository extends LogicRepository<RabbitQueueInfo, String> {
    RabbitQueueInfo findByTenantIdAndClientType(String tenantId,String clientType);
}
