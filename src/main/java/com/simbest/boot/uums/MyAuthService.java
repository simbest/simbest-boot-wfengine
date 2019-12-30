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
        SimplePermission sp = new SimplePermission();
        sp.setId("P1");
        sp.setDescription("应用管理");
        sp.setDisplayOrder(2);
        sp.setIcon("app");
        sp.setMenuLevel(1);
        sp.setPermissionCode("app:module");
        sp.setUrl("/app");
        permissions.add(sp);
        ((SimpleUser) user).setAuthPermissions(permissions);

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
