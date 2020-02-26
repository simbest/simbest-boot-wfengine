package com.simbest.boot.wfengine.provide.tasks.service.impl;/**
 * @author Administrator
 * @create 2019/12/3 21:58.
 */

import com.simbest.boot.wfengine.api.BaseFlowableProcessApi;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.wfengine.provide.tasks.service.IProcessTasksService;
import com.simbest.boot.util.json.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
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
@Service
public class ProcessTasksServiceImpl implements IProcessTasksService {

    @Autowired
    private BaseFlowableProcessApi baseFlowableProcessApi;

    @Override
    public int tasksAddComment(String currentUserCode,String taskId, String processInstanceId, String comment) {
        int i = 1;
        try {
            if(taskId!=null){
                Authentication.setAuthenticatedUserId(currentUserCode);
                baseFlowableProcessApi.getTaskService().addComment(taskId,processInstanceId,comment);
            }else{
                return 0;
            }
        }catch (Exception e ){
            log.error(Exceptions.getStackTraceAsString(e));
            return 0;
        }
        return i;
    }

    @Override
    public int tasksComplete(String taskId, String stringJson) {
        int i = 1;
        try {
            Map<String,Object> var = JacksonUtils.json2obj(stringJson,HashedMap.class);
            if(taskId!=null){
                baseFlowableProcessApi.getTaskService().complete(taskId,var);
            }else{
                return 0;
            }
        }catch (Exception e ){
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
        if(tenantId!=null){
            query.taskTenantId(tenantId);
        }
        if(assignee!=null){
            query.taskAssignee(assignee);
        }
       if(name!=null){
           query.taskName(name);
       }
       if(taskDefinitionKey!=null){
            query.taskDefinitionKey(taskDefinitionKey);
        }
        List<Task> list = query.orderByTaskCreateTime()
                .desc()
                .list();
        List<ProcessTasksInfo> result = new ArrayList<ProcessTasksInfo>();
        for(Task t : list){
            ProcessTasksInfo target = new ProcessTasksInfo();
            BeanUtils.copyProperties(t,target);
            result.add(target);
        }
        return result;
    }

    @Override
    public ProcessTasksInfo tasksGet(String taskId) {
        ProcessTasksInfo target = new ProcessTasksInfo();
        Task task = baseFlowableProcessApi.getTaskService().createTaskQuery().taskId(taskId).singleResult();
        BeanUtils.copyProperties(task,target);
        return target;
    }


}
