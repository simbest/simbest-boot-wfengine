package com.simbest.boot.wfengine.provide.tasks.web;/**
 * @author Administrator
 * @create 2019/12/3 21:57.
 */

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.util.MapUtil;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.wfengine.provide.tasks.model.NewTaskInfo;
import com.simbest.boot.wfengine.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.wfengine.provide.tasks.service.IProcessTasksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceTaskDefinitionKey", value = "上一个任务办理环节", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "assignees", value = "办理人多人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskName", value = "办理环节名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskDefinitionKey", value = "办理环节key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "variables", value = "任务参数", dataType = "Map", paramType = "query")
    })
    @PostMapping(value = {"/createTaskEntityImpls","/sso/createTaskEntityImpls","/api/createTaskEntityImpls","/anonymous/createTaskEntityImpls"})
    //public JsonResponse createTaskEntityImpls (@RequestBody Map<String,Object> map) throws Exception {
    public JsonResponse createTaskEntityImpls (@RequestParam String sourceTaskDefinitionKey,
                                               @RequestParam String assignees,
                                               @RequestParam String taskName,
                                               @RequestParam String taskDefinitionKey,
                                               @RequestParam String processDefinitionId,
                                               @RequestParam String processInstanceId,
                                               @RequestParam String tenantId,
                                               @RequestParam String fromTaskId,
                                               @RequestParam String participantIdentitys) {
        List<String> assigneeList = Arrays.asList(assignees.split(","));
        Map<String , Object> variables = Dict.create()
                .set("tenantId", tenantId)
                .set("processDefinitionId", processDefinitionId)
                .set("fromTaskId", fromTaskId)
                .set("participantIdentitys", participantIdentitys);
        processTasksService.createTaskEntityImpls(sourceTaskDefinitionKey,
                assigneeList,
                taskName,
                taskDefinitionKey,
                processInstanceId,
                processDefinitionId,
                tenantId,
                variables);
        /*log.warn("接收参数【{}】" , map.toString());
        NewTaskInfo newTaskInfo = (NewTaskInfo) MapUtil.mapToObject(map, NewTaskInfo.class);
        processTasksService.createTaskEntityImpls(newTaskInfo.getSourceTaskDefinitionKey(),
                newTaskInfo.getAssignees(),
                newTaskInfo.getTaskName(),
                newTaskInfo.getTaskDefinitionKey(),
                newTaskInfo.getProcessInstanceId(),
                newTaskInfo.getProcessDefinitionId(),
                newTaskInfo.getTenantId(),
                newTaskInfo.getVariables());*/
        return JsonResponse.success("操作成功！");
    }

    @ApiOperation(value = "手动创建任务（单人）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceTaskDefinitionKey", value = "上一个任务办理环节", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "assignee", value = "办理人", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskName", value = "办理环节名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "taskDefinitionKey", value = "办理环节key", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "variables", value = "任务参数", dataType = "Map", paramType = "query")
    })
    @PostMapping(value = {"/createTaskEntityImpl","/sso/createTaskEntityImpl","/api/createTaskEntityImpl","/anonymous/createTaskEntityImpl"})
    //public JsonResponse createTaskEntityImpl (@RequestBody Map<String,Object> map) throws Exception {
    public JsonResponse createTaskEntityImpl (@RequestParam String sourceTaskDefinitionKey,
                                              @RequestParam String assignee,
                                              @RequestParam String taskName,
                                              @RequestParam String taskDefinitionKey,
                                              @RequestParam String processDefinitionId,
                                              @RequestParam String processInstanceId,
                                              @RequestParam String tenantId,
                                              @RequestParam String fromTaskId,
                                              @RequestParam String participantIdentitys) {
        Map<String , Object> variables = Dict.create()
                .set("tenantId", tenantId)
                .set("processDefinitionId", processDefinitionId)
                .set("fromTaskId", fromTaskId)
                .set("participantIdentitys", participantIdentitys);
        processTasksService.createTaskEntityImpl(sourceTaskDefinitionKey,
                assignee,
                taskName,
                taskDefinitionKey,
                processInstanceId,
                processDefinitionId,
                tenantId,
                variables);
        /*processTasksService.createTaskEntityImpl(newTaskInfo.getSourceTaskDefinitionKey(),
                newTaskInfo.getAssignee(),
                newTaskInfo.getTaskName(),
                newTaskInfo.getTaskDefinitionKey(),
                newTaskInfo.getProcessInstanceId(),
                newTaskInfo.getProcessDefinitionId(),
                newTaskInfo.getTenantId(),
                newTaskInfo.getVariables());*/
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

    @ApiOperation(value = "多实例加签")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务Id", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/addMultiInstanceExecution","/sso/addMultiInstanceExecution","/api/addMultiInstanceExecution","/anonymous/addMultiInstanceExecution"})
    public JsonResponse addMultiInstanceExecution (String activityId,String processInstanceId,String stringJson) {
        Map<String,Object> vars = JacksonUtils.json2obj(stringJson,HashedMap.class);
        vars.put("duo_shi_li_jia_qian",processInstanceId);
        processTasksService.addMultiInstanceExecution(activityId,processInstanceId,vars);
        return JsonResponse.success("操作成功！");
    }

    @ApiOperation(value = "多实例减签")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务Id", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deleteMultiInstanceExecution","/sso/deleteMultiInstanceExecution","/api/deleteMultiInstanceExecution","/anonymous/deleteMultiInstanceExecution"})
    public JsonResponse deleteMultiInstanceExecution (String taskId) {
        processTasksService.deleteMultiInstanceExecution(taskId,false);
        return JsonResponse.success("操作成功！");
    }

    @ApiOperation(value = "获取当前环节出去的连线")
    @ApiImplicitParams({@ApiImplicitParam(name = "taskId", value = "任务Id", dataType = "String", paramType = "query") })
    @PostMapping(value = {"/getNextFlowNodes","/sso/getNextFlowNodes","/api/getNextFlowNodes","/anonymous/getNextFlowNodes"})
    public JsonResponse getNextFlowNodes(@RequestParam(required = false) String taskId) {
        List<Map<String,Object>> nextNodes = processTasksService.getNextFlowNodes(taskId);
        return JsonResponse.success(nextNodes);
    }
}
