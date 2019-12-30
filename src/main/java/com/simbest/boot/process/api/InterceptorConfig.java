package com.simbest.boot.process.api;/**
 * @author Administrator
 * @create 2019/12/25 15:07.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 *@ClassName InterceptorConfig
 *@Description CommonInterceptor 的配置类
 *@Author Administrator
 *@Date 2019/12/25 15:07
 *@Version 1.0
 **/
@Configuration
public class InterceptorConfig extends  WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getCommonInterceptor()).addPathPatterns("/app/provide/**");
        super.addInterceptors(registry);
    }

    @Bean
    public CommonInterceptor getCommonInterceptor(){
        return new CommonInterceptor();
    }
}