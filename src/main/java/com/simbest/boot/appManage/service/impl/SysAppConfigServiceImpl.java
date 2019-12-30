package com.simbest.boot.appManage.service.impl;/**
 * @author Administrator
 * @create 2019/12/23 11:38.
 */

import com.github.wenhao.jpa.Specifications;
import com.simbest.boot.appManage.model.SysAppConfig;
import com.simbest.boot.appManage.repository.SysAppConfigRepository;
import com.simbest.boot.appManage.service.ISysAppConfigService;
import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.process.api.CommonInterceptor;
import com.simbest.boot.wfengine.util.Md5Token;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *@ClassName SysAppConfigServiceImpl
 *@Description TODO
 *@Author Administrator
 *@Date 2019/12/23 11:38
 *@Version 1.0
 **/
@Service
@Slf4j
public class SysAppConfigServiceImpl extends LogicService<SysAppConfig,String> implements ISysAppConfigService {

    private SysAppConfigRepository repository;

    @Autowired
    public SysAppConfigServiceImpl(SysAppConfigRepository repository) {
        super(repository);
        this.repository = repository;
    }

    /**
     * 查询基本配置
     *
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @return
     */
    @Override
    public Page<SysAppConfig> getSysAppConfig(int page, int size, String direction, String properties) {
        Specification<SysAppConfig> specification = Specifications.<SysAppConfig>and()
                .eq("removedTime", null)
                .build();

        if (StringUtils.isNotEmpty(direction)) {
            direction = "asc";
        }

        if (StringUtils.isNotEmpty(properties)) {
            properties = "createdTime";
        }

        Pageable pageable = this.getPageable(page, size, direction, properties);
        return this.findAll(specification, pageable);
    }

    /**
     * 第一次调用时，将所有客户端配置加入内存中
     * @param mapConfig
     * @return
     */
    @Override
    public Map<String,SysAppConfig> loadConfig(Map<String,SysAppConfig> mapConfig){
        mapConfig = new HashMap<String,SysAppConfig>();
        List<SysAppConfig> list = getAllEnable();
        if(list!=null && list.size()>0){
            for(SysAppConfig ssc :list){
                mapConfig.put(ssc.getAppCode(),ssc);
            }
        }
        return mapConfig;
    }

    /**
     * 获取有效的客户端配置数据
     * @return
     */
    @Override
    public List<SysAppConfig> getAllEnable() {

        Specification<SysAppConfig> specification = Specifications.<SysAppConfig>and()
                .eq("removedTime", null)
                .eq("enabled", true)
                .build();
        return findAllNoPage(specification);
    }

    public SysAppConfig findByAppCode(String appCode) {
        return repository.findByAppCode(appCode);
    }

    @Override
    public SysAppConfig insert(SysAppConfig o) {
        super.insert(o);
        if(CommonInterceptor.mapConfig!=null){
            CommonInterceptor.mapConfig.put(o.getAppCode(),o);
        }
        return o;
    }

    @Override
    public SysAppConfig update(SysAppConfig o) {
        SysAppConfig appConfig = super.update(o);
        if(o.getAccessToken()!=null){
            if(CommonInterceptor.mapConfig!=null){
                CommonInterceptor.mapConfig.put(o.getAppCode(),appConfig);
            }
        }
        return o;
    }

    /**
     * 获取新token
     * @param appCode 编码
     * @param flag 1自动，2手动
     * @param accessToken 手动生成token
     * @param periodValidity 设置token有效期
     * @return
     */
    @Override
    public SysAppConfig getNewToken(String appCode,Integer flag,String accessToken,Date periodValidity) {
        SysAppConfig o = findByAppCode(appCode);
        o.setPeriodValidity(periodValidity);
        if(flag!=null){
            if(flag.intValue()==1){
                String token = Md5Token.getNewToken();
                o.setAccessToken(token);
            }else{
                o.setAccessToken(accessToken);
            }
        }else{
            String token = Md5Token.getNewToken();
            o.setAccessToken(token);
        }
        SysAppConfig appConfig = super.update(o);
        if(CommonInterceptor.mapConfig!=null){
            CommonInterceptor.mapConfig.put(o.getAppCode(),appConfig);
        }
        return o;
    }

}
