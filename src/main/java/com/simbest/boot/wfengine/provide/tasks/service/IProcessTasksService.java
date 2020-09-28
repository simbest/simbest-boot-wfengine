package com.simbest.boot.wfengine.provide.tasks.service;

import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;
import org.flowable.bpmn.model.SequenceFlow;

import java.util.List;
import java.util.Map;

/**
 * <strong>Title : ProcessTasksServiceImpl流程环节实例操作</strong><br>
 * <strong>Description : </strong><br>
 * <strong>Create on : 2020/9/28</strong><br>
 * <strong>Modify on : 2020/9/28</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 */
public interface IProcessTasksService {
    int tasksComplete(String taskId, String stringJson);

    List<ProcessTasksInfo> tasksQuery(Map<String,Object> map);

    ProcessTasksInfo tasksGet(String taskId);

    int tasksAddComment(String currentUserCode,String taskId, String processInstanceId, String comment);

    void tasksComplete4Mq(MqReceive mqReceive);

    /**
     * 自由跳转
     * @param taskId 当前任务id
     * @param processInstanceId 实例id
     * @param targetNodeId 目标节点
     * @param inputUserId 下一办理人
     */
    void freeFlow(String taskId, String processInstanceId, String targetNodeId, String inputUserId);

    /**
     * 手动创建多个任务
     * @param sourceTaskDefinitionKey 上一个任务办理环节
     * @param assignees 多个办理人
     * @param taskName 办理环节名称
     * @param taskDefinitionKey 办理环节key
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return
     */
    public List<String> createTaskEntityImpls(String sourceTaskDefinitionKey,List<String> assignees,String taskName,String taskDefinitionKey,String processInstanceId,String processDefinitionId,String tenantId,Map<String,Object> variables);

    /**
     * 手动创建任务
     * @param sourceTaskDefinitionKey 上一个任务办理环节
     * @param assignee 办理人
     * @param taskName 办理环节名称
     * @param taskDefinitionKey 办理环节key
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return 任务ID
     */
    public String createTaskEntityImpl(String sourceTaskDefinitionKey,String assignee,String taskName,String taskDefinitionKey,String processInstanceId,String processDefinitionId,String tenantId,Map<String,Object> variables);


    /**
     * 完成当前节点，不再流程下一步
     * @param taskId 当前任务Id
     * @return 是否可以调用本方法
     */
    public boolean finshTask(String taskId);

    /**
     * 会签多实例加签
     * @param activityId 活动节点ID
     * @param processInstanceId 父实例ID
     * @param vars 变量
     */
    public void addMultiInstanceExecution(String activityId,String processInstanceId,Map<String,Object> vars);

    /**
     * 会签多实例减签
     * @param taskId 任务ID
     * @param executionIsComplete 是否按完成处理（根据客户业务，建议填入false）
     *                            跟踪源码可知，主要影响nrOfCompletedInstances，nrOfInstances，loopCounter等变量值
     *                            同时，为了尽量减少业务和流程融合，建议业务判断放在业务中做，把最后的业务结果传递给flowable，不使用flowable做业务逻辑
     */
    public void deleteMultiInstanceExecution(String taskId,Boolean executionIsComplete);

    /**
     * 获取两个环节之间的连线
     * @param processInstanceId
     * @param sourceRef
     * @param targetRef
     * @return
     */
    public SequenceFlow getSequenceFlow(String processInstanceId, String sourceRef, String targetRef);

    /**
     * 获取当前环节出去的连线
     * @param taskId    任务ID
     * @return
     */
    List<Map<String,Object>> getNextFlowNodes(String taskId);
}
