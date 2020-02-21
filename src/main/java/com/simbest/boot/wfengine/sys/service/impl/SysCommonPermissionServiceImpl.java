/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.service.impl;

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.wfengine.sys.model.SysCommonPermission;
import com.simbest.boot.wfengine.sys.repository.SysCommonPermissionRepository;
import com.simbest.boot.wfengine.sys.service.ISysCommonPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用途：基础信息管理-权限管理服务层
 * 作者: lishuyi
 * 时间: 2020/2/6  21:57
 */
@Slf4j
@Service
public class SysCommonPermissionServiceImpl extends LogicService<SysCommonPermission, String> implements ISysCommonPermissionService {
    private SysCommonPermissionRepository repository;

    @Autowired
    public SysCommonPermissionServiceImpl(SysCommonPermissionRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
