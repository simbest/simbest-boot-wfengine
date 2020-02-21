/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.service.impl;

import com.simbest.boot.base.service.impl.LogicService;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IPermission;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.SimpleUser;
import com.simbest.boot.wfengine.sys.model.SysCommonPermission;
import com.simbest.boot.wfengine.sys.model.SysCommonRole;
import com.simbest.boot.wfengine.sys.model.SysCommonUser;
import com.simbest.boot.wfengine.sys.repository.SysCommonPermissionRepository;
import com.simbest.boot.wfengine.sys.repository.SysCommonRoleRepository;
import com.simbest.boot.wfengine.sys.repository.SysCommonUserRepository;
import com.simbest.boot.wfengine.sys.service.ISysCommonUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 用途：基础信息管理-用户管理服务层
 * 作者: lishuyi
 * 时间: 2020/2/6  15:17
 */
@Slf4j
@Service
public class SysCommonUserServiceImpl extends LogicService<SysCommonUser, String> implements ISysCommonUserService {

    @Autowired
    private SysCommonRoleRepository roleRepository;

    @Autowired
    private SysCommonPermissionRepository permissionRepository;

    private SysCommonUserRepository repository;

    @Autowired
    public SysCommonUserServiceImpl(SysCommonUserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public IUser findByKey(String keyword, IAuthService.KeyType keyType) {
        SysCommonUser iUser = null;
        switch (keyType) {
            case username:
                iUser = repository.findByUsername(keyword);
                break;
            case preferredMobile:
                iUser = repository.findByPreferredMobile(keyword);
                break;
        }
        if(null == iUser){
            throw new UsernameNotFoundException("keyword: "+keyword+" not found");
        }
        Set<SysCommonRole> roleSet = roleRepository.findByUserId(iUser.getId());
        Set<SysCommonPermission> permissionSet = permissionRepository.findByUserId(iUser.getId());
        iUser.setAuthRoles(roleSet);
        iUser.setAuthPermissions(permissionSet);
        iUser.addAppAuthorities(roleSet);
        iUser.addAppAuthorities(permissionSet);
        return iUser;
    }

    @Override
    public Set<? extends IPermission> findUserPermissionByAppcode(String username, String appcode) {
        return null;
    }

    @Override
    public boolean checkUserAccessApp(String username, String appcode) {
        return true;
    }

    @Override
    public IUser customUserForApp(IUser iUser, String appcode) {
        return null;
    }

    @Override
    public void changeUserSessionByCorp(IUser newuser) {

    }

    @Override
    public int updateUserOpenidAndUnionid(String preferredMobile, String openid, String unionid, String appcode) {
        return 0;
    }

    @Override
    public IUser createUser(String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser user) {
        return null;
    }

    @Override
    public IUser updateUser(String keyword, IAuthService.KeyType keytype, String appcode, SimpleUser user) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByKey(username, IAuthService.KeyType.username);
    }

}
