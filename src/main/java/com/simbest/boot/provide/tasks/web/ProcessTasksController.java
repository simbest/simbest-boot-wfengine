package com.simbest.boot.provide.tasks.web;/**
 * @author Administrator
 * @create 2019/12/3 21:57.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.provide.tasks.model.ProcessTasksInfo;
import com.simbest.boot.provide.tasks.service.IProcessTasksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
