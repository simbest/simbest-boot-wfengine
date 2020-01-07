/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.uums;

import com.simbest.boot.config.AppConfig;
import com.simbest.boot.security.auth.oauth2.Oauth2RedisTokenStore;
import com.simbest.boot.security.auth.service.AbstractAuthService;
import com.simbest.boot.security.auth.service.IAuthUserCacheService;
import com.simbest.boot.util.SpringContextUtil;
import com.simbest.boot.uums.api.user.UumsSysUserinfoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

/**
 * 用途：不得不实现的接口，认证已由UUMS统一管理
 * 作者: lishuyi
 * 时间: 2018/6/11  10:20
 */
@Service
public class MyAuthService extends AbstractAuthService {

    @Autowired
    public MyAuthService(SpringContextUtil springContextUtil, IAuthUserCacheService authUserCacheService, AppConfig appConfig,
                         UumsSysUserinfoApi userinfoApi, RedisIndexedSessionRepository sessionRepository,
                         Oauth2RedisTokenStore oauth2RedisTokenStore) {
        super(springContextUtil, authUserCacheService, appConfig, userinfoApi, sessionRepository, oauth2RedisTokenStore);
    }

}
