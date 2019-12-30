package com.simbest.boot.appManage.repository;

import com.simbest.boot.appManage.model.SysAppConfig;
import com.simbest.boot.base.repository.LogicRepository;

/**
 * @author Administrator
 * @create 2019/12/23 11:35.
 */
public interface SysAppConfigRepository extends LogicRepository<SysAppConfig,String> {
    SysAppConfig findByAppCode(String appCode);
}
