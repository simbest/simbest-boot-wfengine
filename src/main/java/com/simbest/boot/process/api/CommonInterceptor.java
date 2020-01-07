package com.simbest.boot.process.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;
import com.simbest.boot.appManage.model.SysAppConfig;
import com.simbest.boot.appManage.service.ISysAppConfigService;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.wfengine.util.ConstantsUtils;
import com.simbest.boot.wfengine.util.JsonResponse;
import com.simbest.boot.wfengine.util.Md5Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName CommonInterceptor
 * @Description 拦截器，拦截客户端访问/app/provide/**，校验接口1，TOKEN有效期，2，TOKEN是否正确，3，来访IP是否在白名单
 *
 * @Author Administrator
 * @Date 2019/12/12 15:19
 * @Version 1.0
 **/
public class CommonInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);

    @Autowired
    private ISysAppConfigService sysAppConfigServiceImpl;

    /**
     * 全部客户端的配置信息，第一次使用时加载，应用管理模块修改时，同步修改这里
     */
    public static Map<String,SysAppConfig> mapConfig=null;


    /**
     * 在业务处理器处理请求之前被调用
     * 校验1，TOKEN有效期，2，TOKEN是否正确，3，来访IP是否在白名单
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        boolean res = false;

        String TIMESTAMP = request.getHeader("TIMESTAMP");
        String ACCESSTOKEN = request.getHeader("ACCESSTOKEN");
        String SOURCESYSTEMID = request.getParameter("SOURCESYSTEMID");

        /**
         * 获取全部客户端信息
         */
        if (mapConfig == null) {
            mapConfig = sysAppConfigServiceImpl.loadConfig(mapConfig);
        }
        SysAppConfig sysAppConfig = mapConfig.get(SOURCESYSTEMID);

        if(sysAppConfig==null){
            responseFalse(request, response, JsonResponse.CODE_NULL);
            res = false;
        }

        /*1，校验来源系统token有效期*/
        long i = sysAppConfig.getPeriodValidity().getTime()-DateUtil.getNow();
        if(i>=0){
            //验证通过
            log.info("==============token在有效期================");
            res = true;
        }else{
            //验证未通过
            log.info("==============token失效================");
            responseFalse(request, response, JsonResponse.TOKEN_VALIDITY);
            res = false;
        }

        /*2，校验来源系统token是否正确*/
        if(res){
            String token = Md5Token.MD5(sysAppConfig.getAccessToken() + TIMESTAMP);
            if (ACCESSTOKEN != null && ACCESSTOKEN.equals(token)) {
                //验证通过
                log.info("==============token校验正确================");
                res = true;
            } else {
                log.info("==============token校验失败================");
                //验证未通过
                responseFalse(request, response, JsonResponse.TOKEN_ERROR);
                res = false;
            }
        }
        /*3.校验本地系统IP白名单和来源系统IP是否符合*/
        if (res) {
            String remoteAddr = request.getRemoteAddr();//得到来访者的IP地址
            log.info( "来访者的IP为：{}",remoteAddr );
            if(sysAppConfig.getWhiteHost() == null){
                res = true;
            }else if (sysAppConfig.getWhiteHost().contains(remoteAddr)) {
                res = true;
            } else {
                //验证未通过
                responseFalse(request, response, JsonResponse.IP_ERROR);
                res = false;
            }

        }
        log.info( "==============参数检查结果========={}",res );
        return res;

    }

    /**
     * 校验失败，返回客户端错误信息
     * @param request
     * @param response
     * @param ipError
     * @throws IOException
     */
    private void responseFalse(HttpServletRequest request, HttpServletResponse response, String ipError) throws IOException {
        Map<String, Object> result = Maps.newHashMap();
        result.put("errcode", JsonResponse.ERROR_CODE);
        result.put("message", ipError);
        result.put("instanceId", ConstantsUtils.getInstanceId());
        result.put("data", null);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSONUtils.toJSONString(result));
    }

    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("==============执行顺序: 2、postHandle================");
    }
    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("==============执行顺序: 3、afterCompletion================");


    }




}