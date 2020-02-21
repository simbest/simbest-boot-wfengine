package com.simbest.boot.wfengine.sys.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.wfengine.sys.model.SysCommonUser;

/**
 * 用途：基础信息管理-用户管理持久层
 * 作者: lishuyi
 * 时间: 2020/2/6  15:14
 */
public interface SysCommonUserRepository extends LogicRepository<SysCommonUser, String> {

    SysCommonUser findByUsername(String username);

    SysCommonUser findByPreferredMobile(String preferredMobile);

}
