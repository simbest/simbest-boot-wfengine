/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.wfengine.sys.security.filter;

import com.simbest.boot.security.auth.filter.CustomAbstractAuthenticationProcessingFilter;
import com.simbest.boot.security.auth.handle.FailedAccessDeniedHandler;
import com.simbest.boot.security.auth.handle.RestSuccessLoginHandler;
import com.simbest.boot.security.auth.provider.GenericAuthenticationChecker;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * 用途：不基于UUMS的REST登录过滤器
 * 作者: lishuyi
 * 时间: 2019/7/10  16:46
 */
@Slf4j
@Component
@DependsOn({"formSecurityConfigurer", "loginUtils"})
public class RestLoginFilter extends CustomAbstractAuthenticationProcessingFilter {
    private final static String FILTER_PATH = "/restlogin";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RestSuccessLoginHandler restSuccessLoginHandler;

    @Autowired
    private FailedAccessDeniedHandler failedAccessDeniedHandler;

    @Autowired
    private RsaEncryptor encryptor;

    @Autowired
    private GenericAuthenticationChecker genericAuthenticationChecker;

    public RestLoginFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @PostConstruct
    public void init() {
        log.debug("init successfully");
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(FILTER_PATH));
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(restSuccessLoginHandler);
        setAuthenticationFailureHandler(failedAccessDeniedHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            String appcode = this.obtainAppcode(request);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
            if(authentication.isAuthenticated()){
                return genericAuthenticationChecker.authChek(authentication, appcode);
            }
            return authentication;
        }
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    protected String obtainPassword(HttpServletRequest request) {
        return encryptor.decrypt(request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY)).trim();
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY).trim();
    }

    protected String obtainAppcode(HttpServletRequest request) {
        return request.getParameter("appcode").trim();
    }

}
