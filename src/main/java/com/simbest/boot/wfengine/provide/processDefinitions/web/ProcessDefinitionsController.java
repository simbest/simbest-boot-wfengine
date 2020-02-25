package com.simbest.boot.wfengine.provide.processDefinitions.web;/**
 * @author Administrator
 * @create 2019/12/3 18:48.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.wfengine.provide.processDefinitions.service.IProcessDefinitionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessDefinitionsController
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/3 18:48
 *@Version 1.0
 **/
@Api(description = "ProcessDefinitionsController", tags = {"流程管理-流程定义管理"})
@Slf4j
@RestController
@RequestMapping("/app/provide/definitions")
public class ProcessDefinitionsController {

    @Autowired
    private IProcessDefinitionsService processDefinitionsService;

    @ApiOperation(value = "流程定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页容量", dataType = "int", paramType = "query")
    })
    @PostMapping(value = {"/definitionsQuery","/sso/definitionsQuery","/api/definitionsQuery"})
    public JsonResponse definitionsQuery (@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size,
                                          String key, Integer version, String deploymentId,
                                          String category,String name,String tenantId) {
        Map<String,Object> map = Maps.newHashMap();
        map.put( "key",key );
        map.put( "version",version );
        map.put( "deploymentId",deploymentId );
        map.put( "name",name );
        map.put( "category",category);
        map.put( "tenantId", tenantId);
        map.put( "page", page);
        map.put( "size", size);
        List<ProcessDefinition> list = processDefinitionsService.definitionsQuery(map);
        return JsonResponse.success(list);
    }

    @ApiOperation(value = "获得一个流程定义")
    @ApiImplicitParams({@ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/definitionsGet","/sso/definitionsGet","/api/definitionsGet"})
    public JsonResponse definitionsGet (String processDefinitionId) {
        ProcessDefinition processDefinition = processDefinitionsService.definitionsGet(processDefinitionId);
        return JsonResponse.success(processDefinition);
    }

    @ApiOperation(value = "获取流程图，两个参数填任意一个，如果都填写，以processDefinitionId为准")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processDefinitionId", value = "流程定义ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/getDiagram","/sso/getDiagram","/api/getDiagram","/anonymous/getDiagram"})
    public JsonResponse getDiagram (String processDefinitionId,String processInstanceId) {
        InputStream inputStream = processDefinitionsService.getDiagram(processDefinitionId,processInstanceId);
        return JsonResponse.success(inputStream);
    }
}
