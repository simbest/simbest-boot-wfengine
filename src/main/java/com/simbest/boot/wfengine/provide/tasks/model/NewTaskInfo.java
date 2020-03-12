package com.simbest.boot.wfengine.provide.tasks.model;/**
 * @author Administrator
 * @create 2020/2/29 0:40.
 */

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *@ClassName NewTaskInfo
 *@Description TODO
 *@Author Administrator
 *@Date 2020/2/29 0:40
 *@Version 1.0
 **/
@Data
public class NewTaskInfo {

    private String sourceTaskDefinitionKey;
    private String assignee;
    private List<String> assignees;
    private String taskName;
    private String taskDefinitionKey;
    private String processInstanceId;
    private String processDefinitionId;
    private String tenantId;
    private Map<String,Object> variables;
}
