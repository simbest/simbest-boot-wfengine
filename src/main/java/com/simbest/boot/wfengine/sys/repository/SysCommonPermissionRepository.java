/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.wfengine.sys.model.SysCommonPermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * 用途：基础信息管理-权限管理持久层
 * 作者: lishuyi
 * 时间: 2020/2/6  21:54
 */
public interface SysCommonPermissionRepository extends LogicRepository<SysCommonPermission, String> {

    /**
     * 根据应用id和用户名查询用户权限
     *
     * @param userId 用户id
     */
    @Query(value =
            " SELECT p FROM SysCommonPermission p, SysCommonRolePermission rp, SysCommonRole r, SysCommonUserRole ur, SysCommonUser u " +
                    " WHERE p.id=rp.permissionId AND rp.roleId=r.id AND r.id = ur.roleId AND ur.userId=u.id AND u.id=:userId " +
                    " AND p.enabled=true AND rp.enabled=true AND r.enabled=true AND ur.enabled=true AND u.enabled=true " +
                    " AND p.removedTime is null AND rp.removedTime is null AND r.removedTime is null AND ur.removedTime is null " +
                    " AND u.removedTime is null ")
    Set<SysCommonPermission> findByUserId(@Param("userId") String userId);

}
