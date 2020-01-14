package com.simbest.boot.wfengine.provide.tasks.model;/**
 * @author Administrator
 * @create 2019/12/6 16:54.
 */

import lombok.*;

/**
 *@ClassName ProcessTasksInfo
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/6 16:54
 *@Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessTasksInfo {
    private  String id;
    private  String name;
    private  String description;
    private  String priority;
    private  String owner;
    private  String assignee;
    private  String processInstanceId;
    private  String executionId;
    private  String taskDefinitionId;
    private  String processDefinitionId;
    private  String scopeId;
    private  String subScopeId;
    private  String scopeType;
    private  String scopeDefinitionId;
    private  String createTime;
    private  String taskDefinitionKey;
    private  String dueDate;
    private  String category;
    private  String parentTaskId;
    private  String tenantId;
    private  String formKey;
}
