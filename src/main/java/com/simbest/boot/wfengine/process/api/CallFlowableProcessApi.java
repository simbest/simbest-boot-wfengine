package com.simbest.boot.wfengine.process.api;

import com.simbest.boot.wfengine.app.model.SysAppConfig;
import com.simbest.boot.wfengine.app.service.ISysAppConfigService;
import com.simbest.boot.wfengine.util.ConstansURL;
import com.simbest.boot.wfengine.util.EngineWqqueryHttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *@ClassName CallFlowableProcessApi
 *@Description 统一调用客户端配置代码
 * flowable全局监听类把流程数据返回给客户端
 *@Author Administrator
 *@Date 2019/12/5 17:09
 *@Version 1.0
 **/
@Slf4j
@Component
public class CallFlowableProcessApi {
    @Autowired
    private EngineWqqueryHttpService engineWqqueryHttpService;
    @Autowired
    private ISysAppConfigService sysAppConfigServiceImpl;

    /**
     * 任务创建时触发
     * @param tenantId 租户ID，非常重要
     * @param variables 相关参数
     * @return
     */
    public Map<String,Object> task_created(String tenantId,Map<String,Object> variables){
        String appUrl = getUrlByTenantId(tenantId);
        Map<String,Object> data =null;
        Map<String,Object> map = engineWqqueryHttpService.callInterfaceJsonObject(appUrl,ConstansURL.TASK_CREATED,variables);
        if(map!=null){
            data = (Map<String, Object>) map.get("data");
        }
        return data;
    }

    /**
     * 任务完成时触发
     * @param tenantId 租户ID，非常重要
     * @param variables 相关参数
     * @return
     */
    public Map<String,Object> task_completed(String tenantId,Map<String,Object> variables){
        String appUrl = getUrlByTenantId(tenantId);
        Map<String,Object> data =null;
        Map<String,Object> map = engineWqqueryHttpService.callInterfaceJsonObject(appUrl,ConstansURL.TASK_COMPLETED,variables);
        if(map!=null){
            data = (Map<String, Object>) map.get("data");
        }
        return data;
    }

    /**
     * 实例创建时调用
     * @param tenantId 租户ID，非常重要
     * @param variables 相关参数
     * @return
     */
    public Map<String,Object> process_instance_created(String tenantId,Map<String,Object> variables){
        String appUrl = getUrlByTenantId(tenantId);
        Map<String,Object> data =null;
        Map<String,Object> map = engineWqqueryHttpService.callInterfaceJsonObject(appUrl,ConstansURL.PROCESS_INSTANCE_CREATED,variables);
        if(map!=null){
            data = (Map<String, Object>) map.get("data");
        }
        return data;
    }
    /**
     * 实例完成时调用
     * @param tenantId 租户ID，非常重要
     * @param variables 相关参数
     * @return
     */
    public Map<String,Object> process_instance_ended(String tenantId,Map<String,Object> variables){
        String appUrl = getUrlByTenantId(tenantId);
        Map<String,Object> data =null;
        Map<String,Object> map = engineWqqueryHttpService.callInterfaceJsonObject(appUrl,ConstansURL.PROCESS_INSTANCE_ENDED,variables);
        if(map!=null){
            data = (Map<String, Object>) map.get("data");
        }
        return data;
    }

    /*根据租户id获取当前租户的回调域名*/
    private String getUrlByTenantId(String tenantId) {
        Map<String,SysAppConfig> mapConfig = CommonInterceptor.mapConfig;
        if (mapConfig == null) {
            mapConfig = sysAppConfigServiceImpl.loadConfig(mapConfig);
        }
        SysAppConfig sysAppConfig = mapConfig.get(tenantId);
        return sysAppConfig.getAppUrl();
    }
}
