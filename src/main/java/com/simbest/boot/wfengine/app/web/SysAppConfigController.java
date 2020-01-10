package com.simbest.boot.wfengine.app.web;/**
 * @author Administrator
 * @create 2019/12/23 11:40.
 */

import com.google.common.collect.Maps;
import com.simbest.boot.wfengine.app.model.SysAppConfig;
import com.simbest.boot.wfengine.app.service.ISysAppConfigService;
import com.simbest.boot.base.exception.GlobalExceptionRegister;
import com.simbest.boot.base.web.controller.LogicController;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 *@ClassName SysAppConfigController
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/23 11:40
 *@Version 1.0
 **/
@Api(description = "SysAppConfigController", tags = {"应用管理-基本配置管理"})
@Slf4j
@RestController
@RequestMapping("/sys/appConfig")
public class SysAppConfigController extends LogicController<SysAppConfig,String> {
    private ISysAppConfigService service;

    @Autowired
    public SysAppConfigController(ISysAppConfigService service) {
        super(service);
        this.service = service;
    }

    /**
     * 查询配置
     * @param page              当前页码
     * @param size              每页数量
     * @param direction         排序规则（asc/desc）
     * @param properties        排序规则（属性名称）
     * @return
     */
    @ApiOperation(value = "查询配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", required = true, example = "1"),
            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", required = true, example = "10"),
            @ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/getSysAppConfig","/sso/getSysAppConfig","/api/getSysAppConfig"})
    public JsonResponse getSysAppConfig(@RequestParam(required = false, defaultValue = "1") int page, //当前页码
                                    @RequestParam(required = false, defaultValue = "10") int size, //每页数量
                                    @RequestParam(required = false) String direction, //排序规则（asc/desc）
                                    @RequestParam(required = false) String properties //排序规则（属性名称）
                                     ) {
        return JsonResponse.success(service.getSysAppConfig(page, size, direction, properties));
    }

    /**
     *
     * @param appCode 编码
     * @param flag 1自动，2手动
     * @param accessToken 手动生成token
     * @param periodValidity 设置token有效期
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新TOKEN")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appCode", value = "编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "flag", value = "生成方式1自动，2手动", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "accessToken", value = "手动设置token", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "periodValidity", value = "设置token有效期", dataType = "String", paramType = "query")
    })
    @PostMapping(value = {"/getNewToken","/sso/getNewToken","/api/getNewToken"})
    public JsonResponse getNewToken(@RequestBody(required = false) Map<String,Object> para) throws Exception {
        Map<String, Object> map = Maps.newHashMap();
        try {
            String appCode = (String) para.get("appCode");
            Integer flag = (Integer) para.get("flag");
            String accessToken = (String) para.get("accessToken");
            Date periodValidity = DateUtil.parseCustomDate((String) para.get("periodValidity"),DateUtil.timestampPattern1);
            return JsonResponse.success(service.getNewToken(appCode,flag,accessToken,periodValidity));
        }catch (Exception e) {
            return GlobalExceptionRegister.returnErrorResponse(e);
        }
    }

    @Override
    public JsonResponse findById(String id) {
        return super.findById(id);
    }
}
