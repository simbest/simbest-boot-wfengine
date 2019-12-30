package com.simbest.boot.provide.processInstances.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 19:31.
 */

import com.simbest.boot.api.BaseFlowableProcessApi;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.provide.processInstances.service.IProcessInstancesService;
import com.simbest.boot.util.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessInstancesServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 19:31
 *@Version 1.0
 **/
@Slf4j
@Service
public class ProcessInstancesServiceImpl implements IProcessInstancesService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;
    /**
     * 启动流程实例
     *
     * @param processDefinitionId
     * @param processDefinitionKey
     * @param message
     * @param stringJson
     * @return
     */
    @Override
    public Map<String,Object> instancesStart(String processDefinitionId, String processDefinitionKey, String message, String tenantId,String stringJson) {

        Map<String,Object> result = new HashMap();
        try {
            String processInstanceId = null;
            Map<String,Object> var = JacksonUtils.json2obj(stringJson,HashedMap.class);
            if(processDefinitionId!=null){
                ProcessInstance pi =baseFlowableProcessApi.getRuntimeService().startProcessInstanceById(processDefinitionId,var);
                processInstanceId = pi.getId();
            }else if(processDefinitionKey!=null){
                ProcessInstance pi = baseFlowableProcessApi.getRuntimeService().startProcessInstanceByKeyAndTenantId(processDefinitionKey, var,tenantId);
                processInstanceId = pi.getId();
            }else if(message!=null){
                ProcessInstance pi = baseFlowableProcessApi.getRuntimeService().startProcessInstanceByMessageAndTenantId(message,var,tenantId);
                processInstanceId = pi.getId();
            }
            result.put("processInstanceId",processInstanceId);
        }catch (Exception e ){
            log.error(Exceptions.getStackTraceAsString(e));
        }
        return result;
    }

    @Override
    public List<ProcessInstance> instancesQuery(Map<String, Object> map) {
        String processDefinitionKey = (String) map.get("processDefinitionKey");
        String processDefinitionId = (String) map.get("processDefinitionId");
        String businessKey = (String) map.get("businessKey");
        String tenantId = (String) map.get("tenantId");
        String processInstanceId = (String) map.get("processInstanceId");
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        List<ProcessInstance> list = baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .processDefinitionKey(processDefinitionKey)
                .processDefinitionId(processDefinitionId)
                .processInstanceBusinessKey(businessKey)
                .processInstanceTenantId(tenantId)
                .orderByStartTime()
                .desc()
                .listPage(page,size);
        return list;
    }

    @Override
    public ProcessInstance instancesGet(String processInstanceId) {
        return baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }
}
