package com.simbest.boot.wfengine.app.service;

import com.simbest.boot.wfengine.app.model.SysAppConfig;
import com.simbest.boot.base.service.ILogicService;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @create 2019/12/23 11:37.
 */
public interface ISysAppConfigService extends ILogicService<SysAppConfig,String> {

    /**
     * 查询基本配置
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @return
     */
    Page<SysAppConfig> getSysAppConfig(int page, int size, String direction, String properties);

    /**
     * 第一次调用时，将所有客户端配置加入内存中
     * @param mapConfig
     * @return
     */
    Map<String,SysAppConfig> loadConfig(Map<String,SysAppConfig> mapConfig);

    /**
     * 获取有效的客户端配置数据
     * @return
     */
    List<SysAppConfig> getAllEnable();

    /**
     * 获取新token
     * @param appCode 编码
     * @param flag 1自动，2手动
     * @param accessToken 手动生成token
     * @param periodValidity 设置token有效期
     * @return
     */
    SysAppConfig getNewToken(String appCode,Integer flag,String accessToken,Date periodValidity);
}
