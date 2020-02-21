package com.simbest.boot.wfengine.sys.service;

import com.simbest.boot.base.service.ILogicService;
import com.simbest.boot.wfengine.sys.model.SysCommonUser;
import com.simbest.boot.security.IAuthService;

/**
 * 用途：基础信息管理-用户管理服务层
 * 作者: lishuyi
 * 时间: 2020/2/6  15:16
 */
public interface ISysCommonUserService extends ILogicService<SysCommonUser, String>, IAuthService {

}
