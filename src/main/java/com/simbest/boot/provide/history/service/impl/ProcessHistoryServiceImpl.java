package com.simbest.boot.provide.history.service.impl;/**
 * @author Administrator
 * @create 2019/12/4 11:21.
 */

import com.simbest.boot.api.BaseFlowableProcessApi;
import com.simbest.boot.provide.history.service.IProcessHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessHistoryServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/4 11:21
 *@Version 1.0
 **/
@Slf4j
@Service
public class ProcessHistoryServiceImpl implements IProcessHistoryService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;
    @Override
    public HistoricProcessInstance historyInstancesGet(String processInstanceId) {
        return baseFlowableProcessApi.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> historyInstancesQuery(Map<String, Object> map) {
        String processDefinitionKey = (String) map.get("processDefinitionKey");
        String processDefinitionId = (String) map.get("processDefinitionId");
        String businessKey = (String) map.get("businessKey");
        String tenantId = (String) map.get("tenantId");
        String processInstanceId = (String) map.get("processInstanceId");
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        List<HistoricProcessInstance> list = baseFlowableProcessApi.getHistoryService().createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .processDefinitionKey(processDefinitionKey)
                .processDefinitionId(processDefinitionId)
                .processInstanceBusinessKey(businessKey)
                .processInstanceTenantId(tenantId)
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage(page,size);
        return list;
    }
}
