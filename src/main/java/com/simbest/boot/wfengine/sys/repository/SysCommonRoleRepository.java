package com.simbest.boot.wfengine.sys.repository;

import com.simbest.boot.base.repository.LogicRepository;
import com.simbest.boot.wfengine.sys.model.SysCommonRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * 用途：基础信息管理-角色管理持久层
 * 作者: lishuyi
 * 时间: 2020/2/6  15:14
 */
public interface SysCommonRoleRepository extends LogicRepository<SysCommonRole, String> {

    /**
     * 根据应用id和用户名查询用户角色
     *
     * @param userId 用户id
     */
    @Query(value =
            " SELECT r FROM SysCommonRole r, SysCommonUserRole ur, SysCommonUser u " +
                    " WHERE r.id = ur.roleId AND ur.userId=u.id AND u.id=:userId " +
                    " AND r.enabled=true AND ur.enabled=true AND u.enabled=true " +
                    " AND r.removedTime is null AND ur.removedTime is null AND u.removedTime is null ")
    Set<SysCommonRole> findByUserId(@Param("userId") String userId);

}
