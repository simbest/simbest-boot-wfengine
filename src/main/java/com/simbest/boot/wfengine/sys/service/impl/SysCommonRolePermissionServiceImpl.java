/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.service.impl;

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.wfengine.sys.model.SysCommonRolePermission;
import com.simbest.boot.wfengine.sys.repository.SysCommonRolePermissionRepository;
import com.simbest.boot.wfengine.sys.service.ISysCommonRolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用途：基础信息管理-角色权限管理服务层
 * 作者: lishuyi
 * 时间: 2020/2/6  21:57
 */
@Slf4j
@Service
public class SysCommonRolePermissionServiceImpl extends LogicService<SysCommonRolePermission, String> implements ISysCommonRolePermissionService {
    private SysCommonRolePermissionRepository repository;

    @Autowired
    public SysCommonRolePermissionServiceImpl(SysCommonRolePermissionRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
