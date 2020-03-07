package com.simbest.boot.wfengine.provide.tasks.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 21:58.
 */

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.util.CodeGenerator;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.LoginUtils;
import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.wfengine.process.api.CallFlowableProcessApi;
import com.simbest.boot.wfengine.process.cmd.*;
import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.wfengine.provide.tasks.service.IProcessTasksService;
import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;
import com.simbest.boot.wfengine.rabbitmq.model.MqSend;
import com.simbest.boot.wfengine.rabbitmq.service.IMqSendService;
import com.simbest.boot.wfengine.util.ConstansAction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.impl.persistence.entity.ActivityInstanceEntityImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.HistoricActivityInstanceEntityImpl;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessTasksServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 21:58
 *@Version 1.0
 **/
@Slf4j
@Service("processTasksServiceImpl")
public class ProcessTasksServiceImpl implements IProcessTasksService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    @Autowired
    private LoginUtils loginUtils;

    @Autowired
    private IMqSendService mqSendServiceImpl;

    @Autowired
    private CallFlowableProcessApi callFlowableProcessApi;

    @Override
    public int tasksAddComment(String currentUserCode, String taskId, String processInstanceId, String comment) {
        int i = 1;
        try {
            if (taskId != null) {
                Task task = baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
                if (task.getOwner() != null) {
                    Authentication.setAuthenticatedUserId(task.getOwner());
                    baseFlowableProcessApi.getTaskService().addComment(taskId, processInstanceId, comment);
                } else {
                    Authentication.setAuthenticatedUserId(currentUserCode);
                    baseFlowableProcessApi.getTaskService().addComment(taskId, processInstanceId, comment);
                }

            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(Exceptions.getStackTraceAsString(e));
            return 0;
        }
        return i;
    }

    @Override
    public int tasksComplete(String taskId, String stringJson) {
        int i = 1;
        try {
            Map<String, Object> var = JacksonUtils.json2obj(stringJson, HashedMap.class);
            if (taskId != null) {
                Task task = baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
                if (task.getOwner() != null) {
                    baseFlowableProcessApi.getTaskService().resolveTask(taskId);
                    baseFlowableProcessApi.getTaskService().complete(taskId, var);
                } else {
                    baseFlowableProcessApi.getTaskService().complete(taskId, var);
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            log.error(Exceptions.getStackTraceAsString(e));
            return 0;
        }
        return i;
    }

    @Override
    public List<ProcessTasksInfo> tasksQuery(Map<String, Object> map) {
        String processDefinitionKey = (String) map.get("processDefinitionKey");
        String processDefinitionId = (String) map.get("processDefinitionId");
        String businessKey = (String) map.get("businessKey");
        String tenantId = (String) map.get("tenantId");
        String processInstanceId = (String) map.get("processInstanceId");
        String assignee = (String) map.get("assignee");
        String taskDefinitionKey = (String) map.get("taskDefinitionKey");
        String name = (String) map.get("name");
        String executionId = (String) map.get("executionId");
        //创建任务查询对象
        TaskQuery query = baseFlowableProcessApi.getTaskService().createTaskQuery();
        query.processInstanceId(processInstanceId);
        query.executionId(executionId);
        query.processDefinitionKey(processDefinitionKey);
        query.processDefinitionId(processDefinitionId);
        query.processInstanceBusinessKey(businessKey);
        if (tenantId != null) {
            query.taskTenantId(tenantId);
        }
        if (assignee != null) {
            query.taskAssignee(assignee);
        }
        if (name != null) {
            query.taskName(name);
        }
        if (taskDefinitionKey != null) {
            query.taskDefinitionKey(taskDefinitionKey);
        }
        List<Task> list = query.orderByTaskCreateTime()
                .desc()
                .list();
        List<ProcessTasksInfo> result = new ArrayList<ProcessTasksInfo>();
        for (Task t : list) {
            ProcessTasksInfo target = new ProcessTasksInfo();
            BeanUtils.copyProperties(t, target);
            result.add(target);
        }
        return result;
    }

    @Override
    public ProcessTasksInfo tasksGet(String taskId) {
        ProcessTasksInfo target = new ProcessTasksInfo();
        Task task = baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        BeanUtils.copyProperties(task, target);
        return target;
    }

    @Override
    public void tasksComplete4Mq(MqReceive mqReceive) {
        loginUtils.adminLogin();
        String taskId = mqReceive.getTaskId();
        String mapJson = mqReceive.getMapJson();
        Map<String, Object> startParam = JacksonUtils.json2obj(mapJson, Map.class);

        try {
            Task task = baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
            if (task.getOwner() != null) {
                Authentication.setAuthenticatedUserId(task.getOwner());
                baseFlowableProcessApi.getTaskService().addComment(taskId, mqReceive.getProcessInstId(), (String) startParam.get("message"));
                baseFlowableProcessApi.getTaskService().resolveTask(taskId);
                baseFlowableProcessApi.getTaskService().complete(taskId, startParam);
            } else {
                Authentication.setAuthenticatedUserId((String) startParam.get("currentUserCode"));
                baseFlowableProcessApi.getTaskService().addComment(taskId, mqReceive.getProcessInstId(), (String) startParam.get("message"));
                baseFlowableProcessApi.getTaskService().complete(taskId, startParam);
            }
        } catch (Exception e) {
            log.error(Exceptions.getStackTraceAsString(e));

        }

    }

    /**
     * 自由跳转
     *
     * @param taskId            当前任务id
     * @param processInstanceId 实例id
     * @param targetNodeId      目标节点
     * @param inputUserId       下一办理人
     */
    @Override
    public void freeFlow(String taskId, String processInstanceId, String targetNodeId, String inputUserId) {



        /*手动回传数据给应用*/
        Map<String, Object> map = Maps.newHashMap();
        MqSend mqSend = new MqSend();
        Boolean isRabbit = packData(taskId, map,  mqSend);

        TaskEntityImpl task = (TaskEntityImpl) baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        /*本方法业务逻辑*/
        Map<String, Object> tasksCompleteMap = Maps.newHashMap();
        tasksCompleteMap.put("inputUserId", inputUserId);
        baseFlowableProcessApi.getManagementServices().executeCommand(
                new CommonJumpTaskCmd(taskId,
                        processInstanceId, targetNodeId, tasksCompleteMap));

        /*发送数据*/
        sendData(map, task.getTenantId(), mqSend, isRabbit);

    }


    /**
     * 手动创建多个任务
     *
     * @param assignees           多个办理人
     * @param taskName            办理环节名称
     * @param taskDefinitionKey   办理环节key
     * @param processInstanceId   流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return
     */
    @Override
    public List<String> createTaskEntityImpls(List<String> assignees, String taskName, String taskDefinitionKey, String processInstanceId, String processDefinitionId,String tenantId, Map<String, Object> variables) {
        if (assignees != null) {
            List<String> taskIds = new ArrayList<String>();
            for (String assignee : assignees) {
                taskIds.add(createTaskEntityImpl(assignee, taskName, taskDefinitionKey, processInstanceId, processDefinitionId, tenantId,variables));
            }
            return taskIds;
        }
        return null;

    }

    /**
     * 手动创建任务
     *
     * @param assignee            办理人
     * @param taskName            办理环节名称
     * @param taskDefinitionKey   办理环节key
     * @param processInstanceId   流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return 任务ID
     */
    @Override
    public String createTaskEntityImpl(String assignee, String taskName, String taskDefinitionKey, String processInstanceId, String processDefinitionId,String tenantId, Map<String, Object> variables) {
        String taskId = "TASK" + CodeGenerator.systemUUID();
        /*先创建执行实例*/
        String processDefinitionIdTmp = MapUtil.getStr(variables, "processDefinitionId");
        String executionId = createExecutionEntity(taskDefinitionKey, processInstanceId, processDefinitionId, tenantId, processDefinitionIdTmp);
        baseFlowableProcessApi.getRuntimeService().setVariables(executionId, variables);

        TaskEntityImpl task = (TaskEntityImpl) baseFlowableProcessApi.getTaskService().newTask();
        task.setAssignee(assignee);
        task.setName(taskName);
        task.setTaskDefinitionKey(taskDefinitionKey);
        task.setProcessInstanceId(processInstanceId);
        task.setProcessDefinitionId(processDefinitionId);
        task.setExecutionId(executionId);
        task.setTenantId(tenantId);

        task.setId(taskId);
        baseFlowableProcessApi.getTaskService().saveTask(task);
        log.warn("任务运行实例【{}】>>>>流程定义ID为：{}>>>>>>临时流程定义ID为：{}", task.toString(), task.getProcessDefinitionId(), processDefinitionIdTmp);

        /*创建运行节点*/
        String activityInstanceEntityId = createActivityInstanceEntity(taskId, assignee, taskDefinitionKey, taskName, executionId, processInstanceId, processDefinitionId, tenantId, processDefinitionIdTmp);
        /*创建历史运行节点*/
        createHistoricActivityInstanceEntity(activityInstanceEntityId, taskId, assignee, taskDefinitionKey, taskName, executionId, processInstanceId, processDefinitionId, tenantId,processDefinitionIdTmp);
        return taskId;
    }

    /**
     * 手动创建
     *
     * @param activityId          办理环节key
     * @param processInstanceId   流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return 执行实例ID
     */
    public String createExecutionEntity(String activityId, String processInstanceId, String processDefinitionId, String tenantId, String processDefinitionIdTmp) {
        String executionId = "EXCU" + CodeGenerator.systemUUID();
        ExecutionEntityImpl rootExecution = new ExecutionEntityImpl();
        rootExecution.setProcessDefinitionId(StrUtil.isEmpty(processDefinitionId) ? processDefinitionIdTmp : processDefinitionId);
        rootExecution.setProcessInstanceId(processInstanceId);
        rootExecution.setParentId(processInstanceId);
        rootExecution.setRootProcessInstanceId(processInstanceId);
        rootExecution.setActivityId(activityId);
        rootExecution.setActive(true);
        rootExecution.setMultiInstanceRoot(false);
        rootExecution.setSuspensionState(1);
        rootExecution.setScope(false);
        rootExecution.setTenantId(tenantId);

        rootExecution.setId(executionId);

        baseFlowableProcessApi.getManagementServices().executeCommand(
                new NewExecutionEntityCmd(processInstanceId, rootExecution));
        return executionId;
    }

    /**
     * @param taskId              任务ID
     * @param assignee            办理人
     * @param activityId          办理环节key
     * @param taskName            办理环节名称
     * @param executionId         执行实例ID
     * @param processInstanceId   流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return 流程环节ID
     */
    public String createActivityInstanceEntity(String taskId, String assignee, String activityId, String taskName, String executionId,
                                               String processInstanceId, String processDefinitionId, String tenantId, String processDefinitionIdTmp) {
        String activityInstanceEntityId = "ACTI" + CodeGenerator.systemUUID();
        ActivityInstanceEntityImpl activityInstanceEntity = new ActivityInstanceEntityImpl();
        activityInstanceEntity.setProcessDefinitionId(StrUtil.isEmpty(processDefinitionId) ? processDefinitionIdTmp : processDefinitionId);
        activityInstanceEntity.setProcessInstanceId(processInstanceId);
        activityInstanceEntity.setExecutionId(executionId);
        activityInstanceEntity.setActivityId(activityId);
        activityInstanceEntity.setTaskId(taskId);
        activityInstanceEntity.setActivityName(taskName);
        activityInstanceEntity.setActivityType("userTask");
        activityInstanceEntity.setAssignee(assignee);
        activityInstanceEntity.setStartTime(DateUtil.getCurrent());
        activityInstanceEntity.setId(activityInstanceEntityId);
        activityInstanceEntity.setTenantId(tenantId);

        log.warn("环节运行实例【{}】>>>>流程定义ID为：{}", activityInstanceEntity.toString(), activityInstanceEntity.getProcessDefinitionId());
        baseFlowableProcessApi.getManagementServices().executeCommand(
                new NewActivityInstanceEntityCmd(taskId, activityInstanceEntity));
        return activityInstanceEntityId;

    }

    /**
     * @param activityInstanceEntityId 流程环节ID
     * @param taskId                   任务ID
     * @param assignee                 办理人
     * @param activityId               办理环节key
     * @param taskName                 办理环节名称
     * @param executionId              执行实例ID
     * @param processInstanceId        流程实例ID
     * @param processDefinitionId      流程定义ID
     * @return 流程环节ID
     */
    public String createHistoricActivityInstanceEntity(String activityInstanceEntityId, String taskId, String assignee, String activityId, String taskName,
                                                       String executionId, String processInstanceId, String processDefinitionId,String tenantId,String processDefinitionIdTmp) {
        HistoricActivityInstanceEntityImpl historicActivityInstanceEntity = new HistoricActivityInstanceEntityImpl();
        historicActivityInstanceEntity.setProcessDefinitionId(StrUtil.isEmpty(processDefinitionId) ? processDefinitionIdTmp : processDefinitionId);
        historicActivityInstanceEntity.setProcessInstanceId(processInstanceId);
        historicActivityInstanceEntity.setExecutionId(executionId);
        historicActivityInstanceEntity.setActivityId(activityId);
        historicActivityInstanceEntity.setTaskId(taskId);
        historicActivityInstanceEntity.setActivityName(taskName);
        historicActivityInstanceEntity.setActivityType("userTask");
        historicActivityInstanceEntity.setAssignee(assignee);
        historicActivityInstanceEntity.setStartTime(DateUtil.getCurrent());
        historicActivityInstanceEntity.setId(activityInstanceEntityId);
        historicActivityInstanceEntity.setTenantId(tenantId);

        baseFlowableProcessApi.getManagementServices().executeCommand(
                new NewHistoricActivityInstanceEntityCmd(taskId, historicActivityInstanceEntity));
        return activityInstanceEntityId;

    }

    private boolean finshTask(Task task) {
        List<Execution> executions = baseFlowableProcessApi.getRuntimeService().createExecutionQuery().processInstanceId(task.getProcessInstanceId()).onlyChildExecutions().list();
        if (executions.size() == 1) {
            return false;
        } else {
            baseFlowableProcessApi.getManagementServices().executeCommand(new FinshTaskCmd(task.getId()));
            return true;
        }

    }

    /**
     * 完成当前节点，不再流程下一步
     *
     * @param taskId 当前任务Id
     * @return 是否可以调用本方法
     */
    @Override
    public boolean finshTask(String taskId) {

        /*手动回传数据给应用*/
        Map<String, Object> map = Maps.newHashMap();
        MqSend mqSend = new MqSend();
        Boolean isRabbit = packData(taskId, map, mqSend);

        TaskEntityImpl task = (TaskEntityImpl) baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        boolean result = false;
        result = finshTask(task);

        if (result) {
            /*发送数据*/
            sendData(map, task.getTenantId(), mqSend, isRabbit);
        }
        return result;
    }

    /**
     * 会签多实例加签
     *
     * @param activityId        活动节点ID
     * @param processInstanceId 父实例ID
     * @param vars              变量
     */
    @Override
    public void addMultiInstanceExecution(String activityId, String processInstanceId, Map<String, Object> vars) {
        baseFlowableProcessApi.getRuntimeService().addMultiInstanceExecution(activityId, processInstanceId, vars);
    }

    /**
     * 会签多实例减签
     *
     * @param taskId              任务Id
     * @param executionIsComplete 是否按完成处理（根据客户业务，建议填入false）
     *                            跟踪源码可知，主要影响nrOfCompletedInstances，nrOfInstances，loopCounter等变量值
     *                            同时，为了尽量减少业务和流程融合，建议业务判断放在业务中做，把最后的业务结果传递给flowable，不使用flowable做业务逻辑
     */
    @Override
    public void deleteMultiInstanceExecution(String taskId, Boolean executionIsComplete) {


        /*手动回传数据给应用*/
        Map<String, Object> map = Maps.newHashMap();
        MqSend mqSend = new MqSend();
        Boolean isRabbit = packData(taskId, map , mqSend);

        TaskEntityImpl task = (TaskEntityImpl) baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        /*执行本方法业务逻辑*/
        baseFlowableProcessApi.getRuntimeService().deleteMultiInstanceExecution(task.getExecutionId(), executionIsComplete);

        /*发送数据*/
        sendData(map, task.getTenantId(), mqSend, isRabbit);
    }

    /*发送数据*/
    public void sendData(Map<String, Object> map, String tenantId, MqSend mqSend, Boolean isRabbit) {
        if (!isRabbit) {
            callFlowableProcessApi.task_completed(tenantId, map);
        } else if (isRabbit) {
            //保存消息
            mqSendServiceImpl.insert(mqSend);
            //发送消息到rabbitmq
            mqSendServiceImpl.sendSms(mqSend);
        }
    }

    /*组装数据*/
    public Boolean packData(String taskId, Map<String, Object> map, MqSend mqSend) {
        TaskEntityImpl task = (TaskEntityImpl) baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();

        // 获取流程环节上变量
        Map<String, Object> variables = baseFlowableProcessApi.getTaskService().getVariables(task.getId());
        //String participantIdentity = MapUtil.getStr( variables,"participantIdentity" );
        //String participantIdentitys = MapUtil.getStr( variables,"participantIdentitys" );
        String assignee = task.getAssignee();
        String fromTaskId = MapUtil.getStr(variables, "fromTaskId");

        map.put("tenantId", task.getTenantId());
        map.put("taskId", task.getId());
        map.put("parentTaskId", task.getParentTaskId());
        map.put("taskDefinitionId", task.getTaskDefinitionId());
        map.put("revision", task.getRevision());
        map.put("executionId", task.getExecutionId());
        map.put("processInstId", task.getProcessInstanceId());
        map.put("taskDefinitionKey", task.getTaskDefinitionKey());
        map.put("processDefinitionId", task.getProcessDefinitionId());
        map.put("scopeId", task.getScopeId());
        map.put("subScopeId", task.getSubScopeId());
        map.put("scopeType", task.getScopeType());
        map.put("name", task.getName());
        map.put("description", task.getDescription());
        map.put("owner", task.getOwner());
        map.put("assignee", task.getAssignee());
        map.put("delegationState", task.getDelegationState() != null ? task.getDelegationState().name() : null);
        map.put("priority", task.getPriority());
        map.put("taskCreateTime", task.getCreateTime() != null ? DateUtil.getDate(task.getCreateTime(), DateUtil.timestampPattern1) : null);
        map.put("due", task.getDueDate() != null ? DateUtil.getDate(task.getDueDate(), DateUtil.timestampPattern1) : null);
        map.put("category", task.getCategory());
        map.put("suspensionState", task.getSuspensionState());
        map.put("formKey", task.getFormKey());
        map.put("claimTime", task.getClaimTime() != null ? DateUtil.getDate(task.getClaimTime(), DateUtil.timestampPattern1) : null);
        map.put("fromTaskId", fromTaskId);

        ProcessInstance pi = baseFlowableProcessApi.getRuntimeService().createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();


        /*提交的人的意见还要传回应用本地，方便应用本地存储*/
        map.put("message", (String) variables.get("message"));
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

        if (pi.getName() == null) {
            return false;
        } else if (pi.getName() != null && "rabbitmq".equals(pi.getName())) {
            return true;
        }
        return false;
    }
}
