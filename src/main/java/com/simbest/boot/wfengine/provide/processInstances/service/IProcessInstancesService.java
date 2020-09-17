package com.simbest.boot.wfengine.provide.processInstances.service;

import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @create 2019/12/3 19:38.
 */
public interface IProcessInstancesService {
    /**
     * 启动流程实例
     * @param processDefinitionId
     * @param processDefinitionKey
     * @param message
     * @param stringJson
     * @return
     */
    Map<String,Object> instancesStart(String processDefinitionId, String processDefinitionKey, String message,String tenantId, String stringJson);

    List<ProcessInstance> instancesQuery(Map<String,Object> map);

    ProcessInstance instancesGet(String processInstanceId);

    void instancesStart4Mq(MqReceive mqReceive);

    void deleteProcessInstance(String processInstanceId);

    void upgradeProcessInstanceVersion(String processInstanceIds, String processDefinitionId, Integer version ,String tenantId);

    /**
     * 根据流程定义id校验当前流程版本是否为最新版本
     * @param processInstanceId 实例id
     * @param tenantId          租户id
     * @return
     */
    Map<String, Boolean> checkIsLastVersion(String processInstanceId, String tenantId);
}
