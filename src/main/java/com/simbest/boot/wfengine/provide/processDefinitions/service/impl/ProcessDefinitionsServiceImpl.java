package com.simbest.boot.wfengine.provide.processDefinitions.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 18:49.
 */

import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.wfengine.provide.processDefinitions.service.IProcessDefinitionsService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessDefinitionsServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 18:49
 *@Version 1.0
 **/
@Slf4j
@Service
public class ProcessDefinitionsServiceImpl implements IProcessDefinitionsService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    @Override
    public ProcessDefinition definitionsGet(String processDefinitionId) {
        return baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
    }

    /**
     * 流程定义列表
     *
     * @param map
     * @return
     */
    @Override
    public List<ProcessDefinition> definitionsQuery(Map<String, Object> map) {
        String name = (String) map.get("name");
        String category = (String) map.get("category");
        String tenantId = (String) map.get("tenantId");
        String key = (String) map.get("key");
        Integer version = (Integer) map.get("version");
        String deploymentId = (String) map.get("deploymentId");
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        List<ProcessDefinition> list = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionName(name)
                .processDefinitionId(deploymentId)
                .processDefinitionCategory(category)
                .processDefinitionTenantId(tenantId)
                .processDefinitionKey(key)
                .processDefinitionVersion(version)
                .orderByProcessDefinitionKey()
                .orderByProcessDefinitionVersion()
                .listPage(page,size);
        return list;
    }

    /**
     * 获取流程图
     *
     * @param processDefinitionId
     * @param processInstanceId
     * @return
     */
    @Override
    public InputStream getDiagram(String processDefinitionId, String processInstanceId) {
        ProcessDefinition pd = null;
        if(processDefinitionId!=null){
            pd = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            return baseFlowableProcessApi.getRepositoryService().getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
        }
        if(processInstanceId!=null){
            HistoricProcessInstance historicProcessInstance= baseFlowableProcessApi.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            pd = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
            return baseFlowableProcessApi.getRepositoryService().getResourceAsStream(pd.getDeploymentId(), pd.getDiagramResourceName());
        }
        return null;
    }
}