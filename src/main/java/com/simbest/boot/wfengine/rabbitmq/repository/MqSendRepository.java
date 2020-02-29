package com.simbest.boot.wfengine.rabbitmq.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @create 2020/2/18 10:49.
 */
@Repository
public interface MqSendRepository extends LogicRepository<MqSend, String> {
}
