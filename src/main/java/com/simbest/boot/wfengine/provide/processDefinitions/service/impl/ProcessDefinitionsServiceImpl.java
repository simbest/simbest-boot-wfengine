package com.simbest.boot.wfengine.provide.processDefinitions.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 18:49.
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.wfengine.provide.processDefinitions.service.IProcessDefinitionsService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
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
        ProcessEngineConfiguration engConf = baseFlowableProcessApi.getDefaultProcessEngine().getProcessEngineConfiguration();
        engConf.setActivityFontName( "宋体" );
        engConf.setLabelFontName( "宋体" );
        engConf.setAnnotationFontName("宋体");
        //定义流程画布生成器
        ProcessDiagramGenerator processDiagramGenerator = engConf.getProcessDiagramGenerator();

        if(processDefinitionId!=null){
            pd = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).latestVersion().singleResult();
            BpmnModel bpmnModel = baseFlowableProcessApi.getRepositoryService().getBpmnModel(pd.getId());
            InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);
            return in;
        }
        if(processInstanceId!=null){
            HistoricProcessInstance historicProcessInstance= baseFlowableProcessApi.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            List<HistoricActivityInstance> historyProcess  = baseFlowableProcessApi.getHistoryService() // 历史相关Service
                    .createHistoricActivityInstanceQuery() // 创建历史活动实例查询
                    .processInstanceId(processInstanceId) // 执行流程实例id
                    .finished()
                    .list();
            List<String> activityIds = new ArrayList<>();
            List<String> flows = new ArrayList<>();
            //获取流程图
            BpmnModel bpmnModel = baseFlowableProcessApi.getRepositoryService().getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            for (HistoricActivityInstance hi : historyProcess) {
                String activityType = hi.getActivityType();
                if (activityType.equals("sequenceFlow") || activityType.equals("exclusiveGateway")) {
                    flows.add(hi.getActivityId());
                } else if (activityType.equals("userTask") || activityType.equals("startEvent")) {
                    activityIds.add(hi.getActivityId());
                }
            }
            List<Task> tasks = baseFlowableProcessApi.getTaskService().createTaskQuery().processInstanceId(processInstanceId).list();
            for (Task task : tasks) {
                activityIds.add(task.getTaskDefinitionKey());
            }
            InputStream in = processDiagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engConf.getActivityFontName(), engConf.getLabelFontName(), engConf.getAnnotationFontName(), engConf.getClassLoader(), 1.0, true);

            return in;
        }
        return null;
    }

    /**
     * 根据key获得一个流程定义 ,version可以不填，如果不填，获取最新的返回。
     *
     * @param processDefinitionKey
     * @param version
     * @param tenantId
     * @return
     */
    @Override
    public ProcessDefinition definitionsGetByKey(String processDefinitionKey, String version, String tenantId) {
        if(StrUtil.isNotBlank(processDefinitionKey) && StrUtil.isNotBlank(version)){
            List<ProcessDefinition> list = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionTenantId(tenantId)
                    .processDefinitionKey(processDefinitionKey)
                    .processDefinitionVersion(Integer.parseInt(version))
                    .list();
            if(CollUtil.isNotEmpty(list)){
                return list.get(0);
            }
        }
        if(StrUtil.isNotBlank(processDefinitionKey)){
            List<ProcessDefinition> list = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionTenantId(tenantId)
                    .processDefinitionKey(processDefinitionKey)
                    .latestVersion()
                    .list();
            if(CollUtil.isNotEmpty(list)){
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 根据Key获取流程图，version可以不填，如果不填，获取最新的返回。
     *
     * @param processDefinitionKey
     * @param version
     * @param tenantId
     * @return
     */
    @Override
    public InputStream getDiagramByKey(String processDefinitionKey, String version, String tenantId) {
        ProcessDefinition processDefinition = definitionsGetByKey(processDefinitionKey,version,tenantId);
        if(!StrUtil.isBlankIfStr(processDefinition)){
            return getDiagram(processDefinition.getId(),null);
        }
        return null;
    }

}
