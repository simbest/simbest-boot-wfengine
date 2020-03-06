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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
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


    /**
     * 获取流程图，两个参数填任意一个，如果都填写，以processDefinitionId为准
     * @param processDefinitionId
     * @param processInstanceId
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping(value = {"/getDiagramhttp","/sso/getDiagramhttp","/api/getDiagramhttp","/anonymous/getDiagramhttp"})
    public String getDiagramhttp (String processDefinitionId,String processInstanceId,HttpServletResponse response) throws Exception {
        InputStream in = processDefinitionsService.getDiagram(processDefinitionId, processInstanceId);
        //3：从response对象获取输出流
        OutputStream out = response.getOutputStream();
        //4：将输入流中的数据读取出来，写到输出流中
        for (int b = -1; (b = in.read()) != -1; ) {
            out.write(b);
        }
        out.close();
        in.close();
        //将图写到页面上，用输出流写
        return null;
    }

}
