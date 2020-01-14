package com.simbest.boot.wfengine.provide.processInstances.service;

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
}
