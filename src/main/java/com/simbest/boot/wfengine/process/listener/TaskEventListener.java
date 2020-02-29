package com.simbest.boot.wfengine.process.listener;/**
 * @author Administrator
 * @create 2019/12/9 11:59.
 */

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.LoginUtils;
import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.wfengine.process.api.CallFlowableProcessApi;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;
import com.simbest.boot.wfengine.rabbitmq.service.IMqSendService;
import com.simbest.boot.wfengine.util.ConstansAction;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *@ClassName TaskListener
 *@Description 全局监听配置类，监听任务创建和任务完成
 *@Author Administrator
 *@Date 2019/12/9 11:59
 *@Version 1.0
 **/
@Slf4j
@Component
public class TaskEventListener implements FlowableEventListener {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    @Autowired
    private CallFlowableProcessApi callFlowableProcessApi;

    @Autowired
    private IMqSendService mqSendServiceImpl;

    @Autowired
    private LoginUtils loginUtils;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        loginUtils.adminLogin();
        log.debug("--------------------------------TaskEventListener监听开始-----------------");
        FlowableEngineEventType flowableEventType = (FlowableEngineEventType) flowableEvent.getType();
        FlowableEntityEvent flowableEntityEvent =(FlowableEntityEvent) flowableEvent;
        TaskEntity task = (TaskEntity)flowableEntityEvent.getEntity();
        //log.debug( "流程中的内置环节变量task【{}】", JacksonUtils.obj2json( task ) );
        Map<String ,Object> map=Maps.newHashMap();
        // 获取流程环节上变量
        Map<String,Object> variables = baseFlowableProcessApi.getTaskService().getVariables(task.getId());
        log.debug( "获取流程环节中的参数【{}】", JacksonUtils.obj2json( variables ) );
        String participantIdentity = MapUtil.getStr( variables,"participantIdentity" );
        String participantIdentityStr = MapUtil.getStr( variables, "participantIdentitys" );
        participantIdentityStr = StrUtil.replace( participantIdentityStr,"&quot;", "\"");
        List<Map<String,Object>> participantIdentitys = JacksonUtils.json2Type( participantIdentityStr, new TypeReference<List<Map<String, Object>>>() {} );
        String assignee = task.getAssignee();
        if ( StrUtil.isNotEmpty( participantIdentity ) ){
            map.put("participantIdentity",participantIdentity);
        }
        if ( CollectionUtil.isNotEmpty( participantIdentitys ) ){
            for ( Map<String,Object> participantIdentityMap:participantIdentitys ){
                String participantIdentityTmp = MapUtil.getStr( participantIdentityMap,assignee);
                if ( StrUtil.isNotEmpty(participantIdentityTmp)) {
                    map.put("participantIdentity",participantIdentityTmp);
                    break;
                }
            }
        }
        String fromTaskId = MapUtil.getStr( variables,"fromTaskId" );
        String tenantId = task.getTenantId();
        map.put("tenantId",task.getTenantId());
        map.put("taskId",task.getId());
        map.put("parentTaskId",task.getParentTaskId());
        map.put("taskDefinitionId",task.getTaskDefinitionId());
        map.put("revision",task.getRevision());
        map.put("executionId",task.getExecutionId());
        map.put("processInstId",task.getProcessInstanceId());
        map.put("taskDefinitionKey",task.getTaskDefinitionKey());
        map.put("processDefinitionId",task.getProcessDefinitionId());
        map.put("scopeId",task.getScopeId());
        map.put("subScopeId",task.getSubScopeId());
        map.put("scopeType",task.getScopeType());
        map.put("name",task.getName());
        map.put("description",task.getDescription());
        map.put("owner",task.getOwner());
        map.put("assignee",assignee);
        map.put("delegationState",task.getDelegationState()!=null?task.getDelegationState().name():null);
        map.put("priority",task.getPriority());
        map.put("taskCreateTime",task.getCreateTime()!=null?DateUtil.getDate(task.getCreateTime(),DateUtil.timestampPattern1):null);
        map.put("due",task.getDueDate()!=null?DateUtil.getDate(task.getDueDate(),DateUtil.timestampPattern1):null);
        map.put("category",task.getCategory());
        map.put("suspensionState",task.getSuspensionState());
        map.put("formKey",task.getFormKey());
        map.put("claimTime",task.getClaimTime()!=null?DateUtil.getDate(task.getClaimTime(),DateUtil.timestampPattern1):null);
        map.put( "fromTaskId",fromTaskId );

        ProcessInstance pi =baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        switch (flowableEventType) {
            case TASK_CREATED:
                log.debug("--------------------------------TASK_CREATED监听开始-----------------");
                boolean isRabbit = false;//数据交互方式是否是rabbitmq
                if(pi!=null){//流程发起时，第一个任务创建时，查询不到流程实例
                    if(pi.getName()==null){
                        isRabbit = false;
                    }else if(pi.getName()!=null && "rabbitmq".equals(pi.getName())){
                        isRabbit = true;
                    }
                }else if(variables.get("rabbitmq")!=null){
                    isRabbit = true;
                }
                if(isRabbit){
                    MqSend mqSend = new MqSend();
                    mqSend.setEnabled(true);
                    mqSend.setCreatedTime(DateUtil.date2LocalDateTime(new Date()));
                    if(pi!=null){
                        mqSend.setBusinessKey(pi.getBusinessKey());
                        mqSend.setProcessDefKey(pi.getProcessDefinitionKey() );
                    }else{
                        mqSend.setBusinessKey((String)variables.get("businessKey"));
                        String processDefinitionId = task.getProcessDefinitionId();
                        ProcessDefinition processDefinition = baseFlowableProcessApi.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
                        mqSend.setProcessDefKey(processDefinition.getKey() );
                    }
                    mqSend.setProcessInstId(task.getProcessInstanceId());
                    mqSend.setTaskId(task.getId());
                    mqSend.setTaskDefinitionKey(task.getTaskDefinitionKey());
                    mqSend.setAction(ConstansAction.TASKSCREATELISTENER);
                    mqSend.setTenantId(tenantId);
                    mqSend.setIsSend(0);
                    mqSend.setIsSuccess(-1);//不需要回复成功
                    mqSend.setMapJson(JacksonUtils.obj2json(map));
                    //保存消息
                    mqSendServiceImpl.insert(mqSend);
                    //发送消息到rabbitmq
                    mqSendServiceImpl.sendSms(mqSend);

                }else{
                    callFlowableProcessApi.task_created(tenantId,map);
                }

                break;
            case TASK_ASSIGNED:
                log.debug("--------------------------------TASK_ASSIGNED监听开始-----------------");
                break;
            case TASK_COMPLETED:
                log.debug("--------------------------------TASK_COMPLETED监听开始-----------------");
                if(pi.getName()==null){
                    callFlowableProcessApi.task_completed(tenantId,map);
                }else if(pi.getName()!=null && "rabbitmq".equals(pi.getName())){
                    /*提交的人的意见还要传回应用本地，方便应用本地存储*/
                    map.put("message",(String)variables.get("message"));
                    MqSend mqSend = new MqSend();
                    mqSend.setBusinessKey(pi.getBusinessKey());
                    mqSend.setProcessDefKey(pi.getProcessDefinitionKey());
                    mqSend.setProcessInstId(pi.getProcessInstanceId());
                    mqSend.setTaskId(task.getId());
                    mqSend.setTaskDefinitionKey(task.getTaskDefinitionKey());
                    mqSend.setAction(ConstansAction.TASKSCOMPLETELISTENER);
                    mqSend.setTenantId(task.getTenantId());
                    mqSend.setIsSend(0);
                    mqSend.setIsSuccess(-1);//不需要回复成功
                    mqSend.setMapJson(JacksonUtils.obj2json(map));
                    //保存消息
                    mqSendServiceImpl.insert(mqSend);
                    //发送消息到rabbitmq
                    mqSendServiceImpl.sendSms(mqSend);
                }
                break;
            default:
        }

        log.debug("--------------------------------TaskEventListener监听结束-----------------");
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}
