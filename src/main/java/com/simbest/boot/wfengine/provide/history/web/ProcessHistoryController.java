package com.simbest.boot.wfengine.provide.history.web;/**
 * @author Administrator
 * @create 2019/12/4 11:20.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.wfengine.provide.history.service.IProcessHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessHistoryController
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/4 11:20
 *@Version 1.0
 **/
@Api(description = "ProcessHistoryController", tags = {"流程管理-历史管理"})
@Slf4j
@RestController
@RequestMapping("/app/provide/history")
public class ProcessHistoryController {

    @Autowired
    private IProcessHistoryService processHistoryService;

    @ApiOperation(value = "获得历史流程实例")
    @ApiImplicitParams({@ApiImplicitParam(name = "processInstanceId", value = "实例ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/historyInstancesGet","/sso/historyInstancesGet","/api/historyInstancesGet"})
    public JsonResponse historyInstancesGet (String processInstanceId) {
        HistoricProcessInstance historicProcessInstance = processHistoryService.historyInstancesGet(processInstanceId);
        return JsonResponse.success(historicProcessInstance);
    }

    @ApiOperation(value = "历史流程实例列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页容量", dataType = "int", paramType = "query")
    })
    @PostMapping(value = {"/historyInstancesQuery","/sso/historyInstancesQuery","/api/historyInstancesQuery"})
    public JsonResponse historyInstancesQuery (@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size,
                                    String processDefinitionKey, String processDefinitionId,
                                    String businessKey, String tenantId, String processInstanceId ){
        Map<String,Object> map = Maps.newHashMap();
        map.put( "processDefinitionKey",processDefinitionKey );
        map.put( "processDefinitionId",processDefinitionId);
        map.put( "businessKey", businessKey);
        map.put( "tenantId", tenantId);
        map.put( "processInstanceId", processInstanceId);
        map.put( "page", page);
        map.put( "size", size);
        List<HistoricProcessInstance> list = processHistoryService.historyInstancesQuery(map);
        return JsonResponse.success(list);
    }
}
