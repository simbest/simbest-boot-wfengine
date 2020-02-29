package com.simbest.boot.wfengine.provide.tasks.web;/**
 * @author Administrator
 * @create 2019/12/3 21:57.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.wfengine.provide.tasks.model.NewTaskInfo;
import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.wfengine.provide.tasks.service.IProcessTasksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessTasksController
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 21:57
 *@Version 1.0
 **/
@Api(description = "ProcessTasksController", tags = {"流程管理-任务管理"})
@Slf4j
@RestController
@RequestMapping("/app/provide/tasks")
public class ProcessTasksController {

    @Autowired
    private IProcessTasksService processTasksService;

    @ApiOperation(value = "为任务添加评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务启动", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processInstanceId", value = "实例I", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "currentUserCode", value = "办理人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "comment", value = "评论", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/tasksAddComment","/sso/tasksAddComment","/api/tasksAddComment","/anonymous/tasksAddComment"})
    public JsonResponse tasksAddComment(String currentUserCode,String taskId,String processInstanceId,String comment) {
        try {
            int i = processTasksService.tasksAddComment(currentUserCode,taskId,processInstanceId,comment);
            if(i>0){
                return JsonResponse.success("添加任务评论成功！");
            }else{
                return JsonResponse.fail("添加任务评论失败！");
            }
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @ApiOperation(value = "办理任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务启动", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "stringJson", value = "相关参数", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/tasksComplete","/sso/tasksComplete","/api/tasksComplete","/anonymous/tasksComplete"})
    public JsonResponse tasksComplete(String taskId,String stringJson) {
        try {
            int i = processTasksService.tasksComplete(taskId,stringJson);
            if(i>0){
                return JsonResponse.success("办理任务成功！");
            }else{
                return JsonResponse.fail("办理任务失败！");
            }
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @ApiOperation(value = "查询任务，不分页，不模糊查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "实例ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "assignee", value = "办理人", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "taskDefinitionKey", value = "任务key", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "任务名称", dataType = "int", paramType = "query")
    })
    @PostMapping(value = {"/tasksQueryNoPage","/sso/tasksQueryNoPage","/api/tasksQueryNoPage","/anonymous/tasksQueryNoPage"})
    public JsonResponse tasksQueryNoPage (String processDefinitionKey, String processDefinitionId,
                                        String businessKey, String tenantId, String processInstanceId,
                                        String assignee,String taskDefinitionKey,String name,String executionId) {
        Map<String,Object> map = Maps.newHashMap();
        map.put( "processDefinitionKey",processDefinitionKey );
        map.put( "processDefinitionId",processDefinitionId);
        map.put( "businessKey", businessKey);
        map.put( "tenantId", tenantId);
        map.put( "processInstanceId", processInstanceId);
        map.put( "assignee", assignee);
        map.put( "taskDefinitionKey", taskDefinitionKey);
        map.put( "name", name);
        map.put( "executionId", executionId);
        List<ProcessTasksInfo> list = processTasksService.tasksQuery(map);
        return JsonResponse.success(list);
    }

    @ApiOperation(value = "获得一个任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/tasksGet","/sso/tasksGet","/api/tasksGet","/anonymous/tasksGet"})
    public JsonResponse tasksGet (String taskId) {
        ProcessTasksInfo task = processTasksService.tasksGet(taskId);
        return JsonResponse.success(task);
    }

    @ApiOperation(value = "自由流，撤销，终止,该接口会删除老的任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "processInstanceId", value = "实例id", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "targetNodeId", value = "目标节点", dataType = "String", paramType = "query"),
        @ApiImplicitParam(name = "inputUserId", value = "下一办理人", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/freeFlow","/sso/freeFlow","/api/freeFlow","/anonymous/freeFlow"})
    public JsonResponse freeFlow (String taskId,String processInstanceId,String targetNodeId,String inputUserId) {
        processTasksService.freeFlow(taskId,processInstanceId,targetNodeId,inputUserId);
        return JsonResponse.success("操作成功！");
    }

    @ApiOperation(value = "手动创建任务（多人）")
    @ApiImplicitParams({@ApiImplicitParam(name = "assignees", value = "办理人多人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskName", value = "办理环节名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskDefinitionKey", value = "办理环节key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/createTaskEntityImpls","/sso/createTaskEntityImpls","/api/createTaskEntityImpls","/anonymous/createTaskEntityImpls"})
    public JsonResponse createTaskEntityImpls (@RequestBody NewTaskInfo newTaskInfo) {
        processTasksService.createTaskEntityImpls(newTaskInfo.getAssignees(),
                newTaskInfo.getTaskName(),newTaskInfo.getTaskDefinitionKey(),newTaskInfo.getProcessInstanceId(),newTaskInfo.getProcessDefinitionId());
        return JsonResponse.success("操作成功！");
    }

    @ApiOperation(value = "手动创建任务（单人）")
    @ApiImplicitParams({@ApiImplicitParam(name = "assignee", value = "办理人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskName", value = "办理环节名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskDefinitionKey", value = "办理环节key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/createTaskEntityImpl","/sso/createTaskEntityImpl","/api/createTaskEntityImpl","/anonymous/createTaskEntityImpl"})
    public JsonResponse createTaskEntityImpl (String assignee,String taskName,String taskDefinitionKey,String processInstanceId,String processDefinitionId) {
        processTasksService.createTaskEntityImpl(assignee,taskName,taskDefinitionKey,processInstanceId,processDefinitionId);
        return JsonResponse.success("操作成功！");
    }

    @ApiOperation(value = "完成当前节点，不再流程下一步")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务Id", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/finshTask","/sso/finshTask","/api/finshTask","/anonymous/finshTask"})
    public JsonResponse finshTask (String taskId) {
        boolean result = processTasksService.finshTask(taskId);
        if(result){
            return JsonResponse.success("操作成功！");
        }else{
            return JsonResponse.fail("当前流程只有一个执行实例，请使用常规API办理流程");
        }

    }
}
