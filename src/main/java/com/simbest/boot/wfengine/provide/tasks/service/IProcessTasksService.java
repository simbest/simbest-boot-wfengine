package com.simbest.boot.wfengine.provide.tasks.service;

import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.wfengine.rabbitmq.model.MqReceive;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @create 2019/12/3 21:58.
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
     * @param assignees 多个办理人
     * @param taskName 办理环节名称
     * @param taskDefinitionKey 办理环节key
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return
     */
    public List<String> createTaskEntityImpls(List<String> assignees,String taskName,String taskDefinitionKey,String processInstanceId,String processDefinitionId,Map<String,Object> variables);

    /**
     * 手动创建任务
     * @param assignee 办理人
     * @param taskName 办理环节名称
     * @param taskDefinitionKey 办理环节key
     * @param processInstanceId 流程实例ID
     * @param processDefinitionId 流程定义ID
     * @return 任务ID
     */
    public String createTaskEntityImpl(String assignee,String taskName,String taskDefinitionKey,String processInstanceId,String processDefinitionId,Map<String,Object> variables);


    /**
     * 完成当前节点，不再流程下一步
     * @param taskId 当前任务Id
     * @return 是否可以调用本方法
     */
    public boolean finshTask(String taskId);
}
