package com.simbest.boot.process.management.web;/**
 * @author Administrator
 * @create 2020/1/3 17:54.
 */

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.process.management.dto.CustomTaskInstanceDto;
import com.simbest.boot.process.management.service.ICustomTaskInstanceService;
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
 *@ClassName CustomTaskInstanceController
 *@Description TODO
 *@Author Administrator
 *@Date 2020/1/3 17:54
 *@Version 1.0
 **/
@Api(description = "CustomProcessInstanceController", tags = {"流程查询-流程任务管理"})
@Slf4j
@RestController
@RequestMapping("/processManagement/task")
public class CustomTaskInstanceController {
    @Autowired
    private ICustomTaskInstanceService service;

    @PostMapping({"/findAllCustom", "/sso/findAllCustom", "/api/findAllCustom","/anonymous/findAllCustom"})
    public JsonResponse findAllCustom(@RequestParam(required = false,defaultValue = "1") int page, @RequestParam(required = false,defaultValue = "10") int size,
                                      @RequestParam(required = false) String direction, @RequestParam(required = false) String properties, @RequestBody CustomTaskInstanceDto o) {
        Map<String, Object> mapParams = new HashMap<String, Object>();
        mapParams.put("id",o.getId());
        mapParams.put("procInstId",o.getProcInstId());
        mapParams.put("taskDefKey",o.getTaskDefKey());
        mapParams.put("name",o.getName());
        mapParams.put("assignee",o.getAssignee());
        mapParams.put("tenantId",o.getTenantId());
        Pageable pageable = GetPageable.getPageable(page, size, null, null);
        Page<CustomTaskInstanceDto> pages = this.service.findAllCustom(mapParams, pageable);
        return JsonResponse.success(pages);
    }

}
