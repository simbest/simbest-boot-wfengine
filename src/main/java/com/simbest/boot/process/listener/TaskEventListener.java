package com.simbest.boot.process.listener;/**
 * @author Administrator
 * @create 2019/12/9 11:59.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.process.api.CallFlowableProcessApi;
import com.simbest.boot.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private CallFlowableProcessApi callFlowableProcessApi;

    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        log.debug("--------------------------------TaskEventListener监听开始-----------------");
        FlowableEngineEventType flowableEventType = (FlowableEngineEventType) flowableEvent.getType();
        FlowableEntityEvent flowableEntityEvent =(FlowableEntityEvent) flowableEvent;
        TaskEntity task = (TaskEntity)flowableEntityEvent.getEntity();
        Map<String ,Object> map=Maps.newHashMap();
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
        map.put("assignee",task.getAssignee());
        map.put("delegationState",task.getDelegationState()!=null?task.getDelegationState().name():null);
        map.put("priority",task.getPriority());
        map.put("taskCreateTime",task.getCreateTime()!=null?DateUtil.getDate(task.getCreateTime(),DateUtil.timestampPattern1):null);
        map.put("due",task.getDueDate()!=null?DateUtil.getDate(task.getDueDate(),DateUtil.timestampPattern1):null);
        map.put("category",task.getCategory());
        map.put("suspensionState",task.getSuspensionState());
        map.put("formKey",task.getFormKey());
        map.put("claimTime",task.getClaimTime()!=null?DateUtil.getDate(task.getClaimTime(),DateUtil.timestampPattern1):null);

        switch (flowableEventType) {
            case TASK_CREATED:
                log.debug("--------------------------------TASK_CREATED监听开始-----------------");
                callFlowableProcessApi.task_created(tenantId,map);
                break;
            case TASK_ASSIGNED:
                log.debug("--------------------------------TASK_ASSIGNED监听开始-----------------");
                break;
            case TASK_COMPLETED:
                log.debug("--------------------------------TASK_COMPLETED监听开始-----------------");
                callFlowableProcessApi.task_completed(tenantId,map);
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
