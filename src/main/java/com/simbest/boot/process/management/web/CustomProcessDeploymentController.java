package com.simbest.boot.process.management.web;/**
 * @author Administrator
 * @create 2020/1/3 17:55.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.process.management.dto.CustomProcessDeploymentDto;
import com.simbest.boot.process.management.service.ICustomProcessDeploymentService;
import com.simbest.boot.process.management.service.impl.GetPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName CustomProcessDeploymentController
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:55
 *@Version 1.0
 **/
@Api(description = "CustomProcessDefinitionController", tags = {"流程查询-流程部署管理"})
@Slf4j
@RestController
@RequestMapping("/processManagement/deployment")
public class CustomProcessDeploymentController  {

    @Autowired
    private ICustomProcessDeploymentService service;


    @PostMapping({"/findAllCustom", "/sso/findAllCustom", "/api/findAllCustom","/anonymous/findAllCustom"})
    public JsonResponse findAllCustom(@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int size,
                                      @RequestParam(required = false) String direction, @RequestParam(required = false) String properties, @RequestBody CustomProcessDeploymentDto o) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("name",o.getName());
        mapParams.put("tenantId",o.getTenantId());
        Pageable pageable = GetPageable.getPageable(page, size, null, null);
        Page<CustomProcessDeploymentDto> pages = this.service.findAllCustom(mapParams, pageable);
        return JsonResponse.success(pages);
    }

//    @ApiOperation(value = "创建部署，tenantId必填")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "file", value = "部署文件", dataType = "File", paramType = "query"),
//            @ApiImplicitParam(name = "name", value = "DeploymentName", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "category", value = "分类", dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query")
//    })
//    @PostMapping(value = {"/deploymentsAdd","/sso/deploymentsAdd","/api/deploymentsAdd","/anonymous/deploymentsAdd"})
//    public JsonResponse deploymentsAdd(@RequestParam("file") MultipartFile file, String name, String category, String tenantId) {
//        if(tenantId==null){
//            return JsonResponse.fail("租户tenantId是空的，请检查！");
//        }
//        try {
//            Map<String,Object> map = Maps.newHashMap( );
//            map.put( "name",name );
//            map.put( "category",category);
//            map.put( "tenantId", tenantId);
//            map.put( "file",file );
//            int i = service.deploymentsAdd(map);
//            if(i>0){
//                return JsonResponse.success("部署成功！");
//            }else{
//                return JsonResponse.fail("部署失败！");
//            }
//        } catch (Exception e) {
//            return GlobalExceptionRegister.returnErrorResponse(e);
//        }
//    }

    @ApiOperation(value = "创建部署，tenantId必填")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysFile", value = "部署文件", dataType = "File", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "DeploymentName", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "category", value = "分类", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tenantId", value = "租户ID", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/deploymentsAdd","/sso/deploymentsAdd","/api/deploymentsAdd","/anonymous/deploymentsAdd"})
    public JsonResponse deploymentsAdd(@RequestBody CustomProcessDeploymentDto o) {
        if(o.getTenantId()==null){
            return JsonResponse.fail("租户tenantId是空的，请检查！");
        }
        try {
            Map<String,Object> map = Maps.newHashMap( );
            map.put( "name",o.getName() );
            map.put( "category",o.getCategory());
            map.put( "tenantId", o.getTenantId());
            map.put( "files",o.getFile());
            String message = service.deploymentsAdd(map);
            return JsonResponse.success(message);
        } catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }
}
