package com.simbest.boot.process.management.web;/**
 * @author Administrator
 * @create 2020/1/3 17:55.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.process.management.dto.CustomProcessInstanceDto;
import com.simbest.boot.process.management.service.ICustomProcessInstanceService;
import com.simbest.boot.process.management.service.impl.GetPageable;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *@ClassName CustomProcessInstanceController
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:55
 *@Version 1.0
 **/
@Api(description = "CustomProcessInstanceController", tags = {"流程查询-流程实例管理"})
@Slf4j
@RestController
@RequestMapping("/processManagement/instance")
public class CustomProcessInstanceController  {
    @Autowired
    private ICustomProcessInstanceService service;

    @PostMapping({"/findAllCustom", "/sso/findAllCustom", "/api/findAllCustom","/anonymous/findAllCustom"})
    public JsonResponse findAllCustom(@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int size,
                                      @RequestParam(required = false) String direction, @RequestParam(required = false) String properties, @RequestBody CustomProcessInstanceDto o) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("procInstId",o.getProcInstId());
        mapParams.put("businessKey",o.getBusinessKey());
        mapParams.put("tenantId",o.getTenantId());
        Pageable pageable = GetPageable.getPageable(page, size, null, null);
        Page<CustomProcessInstanceDto> pages = this.service.findAllCustom(mapParams, pageable);
        return JsonResponse.success(pages);
    }

    /**
     * 暂时保留，确定是在wfdriver实现该功能还是wfengine实现
     * @param processInstanceIds
     * @param verion
     * @return
     */
    @PostMapping({"/upgradeProcessInstanceVersion", "/sso/upgradeProcessInstanceVersion", "/api/upgradeProcessInstanceVersion"})
    public JsonResponse upgradeProcessInstanceVersion(String processInstanceIds,Integer verion){
        Map<String, Object> map = Maps.newHashMap();
        try {
//            CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutor();
//            String[] processInstanceIdArray = processInstanceIds.split(",");
//            for(String processInstanceId : processInstanceIdArray){
//                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//                if(processInstance!=null){
//                    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processInstance.getProcessDefinitionKey()).processDefinitionVersion(verion).singleResult();
//                    if(processDefinition!=null){
//                        commandExecutor.execute(new SetProcessDefinitionVersionCmd(processInstanceId, processDefinition.getVersion()));
//                    }
//                }
//            }
            return JsonResponse.success("操作成功");
        } catch (Exception e) {
            log.error(Exceptions.getStackTraceAsString(e));
            return JsonResponse.fail("操作失败");
        }
    }
}
