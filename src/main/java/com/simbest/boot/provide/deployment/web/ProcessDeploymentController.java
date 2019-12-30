package com.simbest.boot.provide.deployment.web;

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.provide.deployment.service.IProcessDeploymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *@ClassName ProcessDeploymentController
 *@Description 流程管理-部署管理
 *@Author Administrator
 *@Date 2019/12/3 16:46
 *@Version 1.0
 **/
@Api(description = "ProcessDeploymentController", tags = {"流程管理-部署管理"})
@Slf4j
@RestController
@RequestMapping("/app/provide/deployment")
public class ProcessDeploymentController {

    @Autowired
    private IProcessDeploymentService processDeploymentService;

    @ApiOperation(value = "创建部署，tenantId必填")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "部署文件", dataType = "File", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "DeploymentName", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "分类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deploymentsAdd","/sso/deploymentsAdd","/api/deploymentsAdd","/anonymous/deploymentsAdd"})
    public JsonResponse deploymentsAdd(@RequestParam("file") MultipartFile file,String name,String category,String tenantId) {
        if(tenantId==null){
            return JsonResponse.fail("租户tenantId是空的，请检查！");
        }
        try {
            Map<String,Object> map = Maps.newHashMap( );
            map.put( "name",name );
            map.put( "category",category);
            map.put( "tenantId", tenantId);
            map.put( "file",file );
            int i = processDeploymentService.deploymentsAdd(map);
            if(i>0){
                return JsonResponse.success("部署成功！");
            }else{
                return JsonResponse.fail("部署失败！");
            }
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @ApiOperation(value = "自动部署专用，tenantId必填")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "部署文件", dataType = "File", paramType = "query"),
            @ApiImplicitParam(name = "filename", value = "部署文件名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "DeploymentName", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "分类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deploymentsAddResource","/sso/deploymentsAddResource","/api/deploymentsAddResource","/anonymous/deploymentsAddResource"})
    public JsonResponse deploymentsAddInputStream(MultipartFile file, String filename, String name, String category, String tenantId) {
        if(tenantId==null){
            return JsonResponse.fail("租户tenantId是空的，请检查！");
        }
        try {
            Map<String,Object> map = Maps.newHashMap( );
            map.put( "name",name );
            map.put( "category",category);
            map.put( "tenantId", tenantId);
            map.put( "file",file );
            map.put( "filename",filename );
            int i = processDeploymentService.deploymentsAddInputStream(map);
            if(i>0){
                return JsonResponse.success("部署成功！");
            }else{
                return JsonResponse.fail("部署失败！");
            }
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @ApiOperation(value = "查询部署列表，tenantId必填")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "页容量", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "DeploymentName", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "分类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deploymentsQuery","/sso/deploymentsQuery","/api/deploymentsQuery","/anonymous/deploymentsQuery"})
    public JsonResponse deploymentsQuery ( @RequestParam(required = false, defaultValue = "1") int page,  @RequestParam(required = false, defaultValue = "10") int size,
                                           String name,String category,String tenantId) {
        if(tenantId==null){
            return JsonResponse.fail("租户tenantId是空的，请检查！");
        }
        Map<String,Object> map = Maps.newHashMap();
        map.put( "name",name );
        map.put( "category",category);
        map.put( "tenantId", tenantId);
        map.put( "page", page);
        map.put( "size", size);
        List<Deployment> list = processDeploymentService.deploymentsQuery(map);
        return JsonResponse.success(list);
    }

    @ApiOperation(value = "获得一个部署")
    @ApiImplicitParams({@ApiImplicitParam(name = "deploymentId", value = "部署ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deploymentsGet","/sso/deploymentsGet","/api/deploymentsGet","/anonymous/deploymentsGet"})
    public JsonResponse deploymentsGet (String deploymentId) {
        Deployment deployment = processDeploymentService.deploymentsGet(deploymentId);
        return JsonResponse.success(deployment);
    }
}
