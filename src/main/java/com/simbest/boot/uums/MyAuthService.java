/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums;

import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IPermission;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.MySimpleGrantedAuthority;
import com.simbest.boot.security.SimplePermission;
import com.simbest.boot.security.SimpleRole;
import com.simbest.boot.security.SimpleUser;
import com.simbest.boot.uums.api.user.UumsSysUserinfoApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 用途：不得不实现的接口，认证已由UUMS统一管理
 * 作者: lishuyi
 * 时间: 2018/6/11  10:20
 */
@Slf4j
@Service
public class MyAuthService implements IAuthService {

    @Value("${logback.artifactId}")
    private String myAppcode;

    @Autowired
    protected UumsSysUserinfoApi userinfoApi;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return getSimpleUser(username);
    }

    /**
     * 微信验证用户
     *
     * @param username 即openid
     * @return
     */
    @Override
    public IUser findByKey(String keyword, KeyType keyType) {
        return getSimpleUser(keyword);
    }

    /**
     * 微信用户权限
     *
     * @param username
     * @param appcode
     * @return
     */
    @Override
    public Set<IPermission> findUserPermissionByAppcode(String username, String appcode) {
        return null;
    }

    /**
     * 是否有访问权限
     *
     * @param username
     * @param appcode
     * @return
     */
    @Override
    public boolean checkUserAccessApp(String username, String appcode) {
        return true;
    }

    @Override
    public IUser customUserForApp(IUser iUser, String appcode) {
        return iUser;
    }

    @Override
    public void changeUserSessionByCorp(IUser newuser) {
    }

    @Override
    public int updateUserOpenidAndUnionid(String preferredMobile, String openid, String unionid, String appcode){
        return 0;
    }


    @Override
    public IUser createUser(String keyword, KeyType keytype, String appcode, SimpleUser user) {
        return null;
    }

    @Override
    public IUser updateUser(String keyword, KeyType keytype, String appcode, SimpleUser user) {
        return null;
    }

    private IUser getSimpleUser(String username){
        IUser user = new SimpleUser();
        ((SimpleUser) user).setId("1");
        ((SimpleUser) user).setAccountNonExpired(true);
        ((SimpleUser) user).setAccountNonLocked(true);
        ((SimpleUser) user).setCredentialsNonExpired(true);
        ((SimpleUser) user).setEnabled(true);
        //openid
        ((SimpleUser) user).setReserve1(username);
        //username
        ((SimpleUser) user).setUsername(username);
        //password
        ((SimpleUser) user).setPassword("$2a$12$qE7F54Gm9lbjphbwOG8N2OsAH03N2R5lsaDaBMp4yY./oRf2w3K9i"); //111.com after rsa
        //用户角色
        Set<SimpleRole> roles = new HashSet<>();
        SimpleRole role1 = new SimpleRole();
        role1.setRoleCode("ROLE_USER");
        roles.add(role1);
        ((SimpleUser) user).setAuthRoles(roles);
        //权限
        Set<SimplePermission> permissions = new HashSet<>();
        SimplePermission sp1 = new SimplePermission();
        sp1.setId("P1");
        sp1.setDescription("应用管理");
        sp1.setDisplayOrder(1);
        sp1.setIcon("app");
        sp1.setMenuLevel(1);
        sp1.setPermissionCode("app:module");
        sp1.setUrl("/app");
        permissions.add(sp1);
        SimplePermission sp2 = new SimplePermission();
        sp2.setId("P2");
        sp2.setDescription("流程管理");
        sp2.setDisplayOrder(2);
        sp2.setIcon("process");
        sp2.setMenuLevel(2);
        sp2.setPermissionCode("process:module");
        sp2.setUrl("/process");
        permissions.add(sp2);
        SimplePermission sp21 = new SimplePermission();
        sp21.setId("P21");
        sp21.setDescription("定义管理");
        sp21.setDisplayOrder(21);
        sp21.setIcon("define");
        sp21.setMenuLevel(3);
        sp21.setPermissionCode("process:define");
        sp21.setUrl("/process/define");
        sp21.setParentId("P2");
        permissions.add(sp21);
        SimplePermission sp22 = new SimplePermission();
        sp22.setId("P22");
        sp22.setDescription("部署管理");
        sp22.setDisplayOrder(22);
        sp22.setIcon("deploy");
        sp22.setMenuLevel(3);
        sp22.setPermissionCode("process:deploy");
        sp22.setUrl("/process/deploy");
        sp22.setParentId("P2");
        permissions.add(sp22);
        SimplePermission sp23 = new SimplePermission();
        sp23.setId("P23");
        sp23.setDescription("实例管理");
        sp23.setDisplayOrder(23);
        sp23.setIcon("instance");
        sp23.setMenuLevel(3);
        sp23.setPermissionCode("process:instance");
        sp23.setUrl("/process/instance");
        sp23.setParentId("P2");
        permissions.add(sp23);
        ((SimpleUser) user).setAuthPermissions(permissions);
        SimplePermission sp24 = new SimplePermission();
        sp24.setId("P24");
        sp24.setDescription("任务管理");
        sp24.setDisplayOrder(24);
        sp24.setIcon("task");
        sp24.setMenuLevel(3);
        sp24.setPermissionCode("process:task");
        sp24.setUrl("/process/task");
        sp24.setParentId("P2");
        permissions.add(sp24);

        Set<MySimpleGrantedAuthority> authoritys = new HashSet<>();
        MySimpleGrantedAuthority a2 = new MySimpleGrantedAuthority("ROLE_USER");
        authoritys.add(a2);
        ((SimpleUser) user).setAuthorities(authoritys);
        return user;
    }

    public void simulationLogin(String username) {
        IUser iUser = getSimpleUser(username);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(iUser, null, iUser.getAuthorities());
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }

}
