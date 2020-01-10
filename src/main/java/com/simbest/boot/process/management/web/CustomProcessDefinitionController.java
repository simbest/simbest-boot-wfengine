package com.simbest.boot.process.management.web;/**
 * @author Administrator
 * @create 2020/1/3 17:54.
 */

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.process.management.dto.CustomProcessDefinitionDto;
import com.simbest.boot.process.management.service.ICustomProcessDefinitionService;
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
 *@ClassName CustomProcessDefinitionController
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:54
 *@Version 1.0
 **/
@Api(description = "CustomProcessDefinitionController", tags = {"流程查询-流程定义管理"})
@Slf4j
@RestController
@RequestMapping("/processManagement/definition")
public class CustomProcessDefinitionController {

    @Autowired
    private ICustomProcessDefinitionService service;

    @PostMapping({"/findAllCustom", "/sso/findAllCustom", "/api/findAllCustom","/anonymous/findAllCustom"})
    public JsonResponse findAllCustom(@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int size,
                                @RequestParam(required = false) String direction, @RequestParam(required = false) String properties, @RequestBody CustomProcessDefinitionDto o) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("key_",o.getKey_());
        mapParams.put("name",o.getName());
        mapParams.put("tenantId",o.getTenantId());
        Pageable pageable = GetPageable.getPageable(page, size, null, null);
        Page<CustomProcessDefinitionDto> pages = this.service.findAllCustom(mapParams, pageable);
        return JsonResponse.success(pages);
    }
}
