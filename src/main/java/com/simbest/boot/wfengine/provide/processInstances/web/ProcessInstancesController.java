package com.simbest.boot.wfengine.provide.processInstances.web;/**
 * @author Administrator
 * @create 2019/12/3 19:20.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.wfengine.provide.processInstances.service.IProcessInstancesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessInstancesWeb
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 19:20
 *@Version 1.0
 **/
@Api(description = "ProcessInstancesController", tags = {"流程管理-流程实例管理"})
@Slf4j
@RestController
@RequestMapping("/app/provide/instances")
public class ProcessInstancesController {


    @Autowired
    private IProcessInstancesService processInstancesService;

    @ApiOperation(value = "启动流程定义")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionId", value = "使用流程定义id启动", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processDefinitionKey", value = "使用流程定义key启动", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "message", value = "使用message启动", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "stringJson", value = "相关参数", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/instancesStart","/sso/instancesStart","/api/instancesStart","/anonymous/instancesStart"})
    public JsonResponse instancesStart(String processDefinitionId, String processDefinitionKey, String message,String tenantId,String stringJson) {
        if(tenantId==null){
            return JsonResponse.fail("租户tenantId是空的，请检查！");
        }
        try {
            Map<String,Object> result = processInstancesService.instancesStart(processDefinitionId,processDefinitionKey,message,tenantId,stringJson);
            return  JsonResponse.success(result,"启动实例成功！");
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @ApiOperation(value = "查询运行中实例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页容量", dataType = "int", paramType = "query")
    })
    @PostMapping(value = {"/instancesQuery","/sso/instancesQuery","/api/instancesQuery","/anonymous/instancesQuery"})
    public JsonResponse instancesQuery (@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size,
                                          String processDefinitionKey, String processDefinitionId, String businessKey, String tenantId,String processInstanceId) {
        if(tenantId==null){
            return JsonResponse.fail("租户tenantId是空的，请检查！");
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put( "processDefinitionKey",processDefinitionKey );
        map.put( "processDefinitionId",processDefinitionId);
        map.put( "businessKey", businessKey);
        map.put( "tenantId", tenantId);
        map.put( "processInstanceId", processInstanceId);
        map.put( "page", page);
        map.put( "size", size);
        List<ProcessInstance> list = processInstancesService.instancesQuery(map);
        return JsonResponse.success(list);
    }

    @ApiOperation(value = "获得一个实例")
    @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "实例ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/instancesGet","/sso/instancesGet","/api/instancesGet","/anonymous/instancesGet"})
    public JsonResponse instancesGet (String processInstanceId) {
        ProcessInstance processInstance = processInstancesService.instancesGet(processInstanceId);
        return JsonResponse.success(processInstance);
    }

    @ApiOperation(value = "删除流程")
    @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "实例ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deleteProcessInstance","/sso/deleteProcessInstance","/api/deleteProcessInstance","/anonymous/deleteProcessInstance"})
    public JsonResponse deleteProcessInstance (String processInstanceId) {
        processInstancesService.deleteProcessInstance(processInstanceId);
        return JsonResponse.success("删除成功！");
    }

    @ApiOperation(value = "升级指定流程的流程定义,流程实例ID如果有多个，逗号分割，流程定义ID和版本号填一个就可以，如果都填写，以流程定义ID为准")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceIds", value = "实例ID（如果是多个，逗号分割）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户id", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/upgradeProcessInstanceVersion","/sso/upgradeProcessInstanceVersion","/api/upgradeProcessInstanceVersion","/anonymous/upgradeProcessInstanceVersion"})
    public JsonResponse upgradeProcessInstanceVersion (String processInstanceIds,String processDefinitionId,Integer version , String tenantId) {
        /*if(processDefinitionId == null && version == null){
            return JsonResponse.fail("流程定义ID和版本号至少要填写一个！");
        }else{
            processInstancesService.upgradeProcessInstanceVersion(processInstanceIds,processDefinitionId,version);
            return JsonResponse.success("升级成功！");
        }*/
        processInstancesService.upgradeProcessInstanceVersion(processInstanceIds,processDefinitionId,version , tenantId);
        return JsonResponse.success("升级成功！");
    }

    @ApiOperation(value = "根据流程定义id校验当前流程版本是否为最新版本")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId" , value = "实例ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "tenantId" , value = "租户id", dataType = "string", paramType = "query"),
    })
    @PostMapping(value = {"/checkIsLastVersion" , "/sso/checkIsLastVersion" , "/api/checkIsLastVersion" , "/anonymous/checkIsLastVersion"})
    public JsonResponse checkIsLastVersion(String processInstanceId , String tenantId) {
        Map<String , Object> map = processInstancesService.checkIsLastVersion(processInstanceId , tenantId);
        return JsonResponse.success(map);
    }
}
