package com.simbest.boot;

import com.simbest.boot.base.repository.CustomJpaRepositoryFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <strong>Title</strong> : SimbestApplication.java<br>
 * <strong>Description</strong> : <br>
 * <strong>Create on</strong> : 2018/1/15<br>
 * <strong>Modify on</strong> : 2018/1/15<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
@EntityScan(basePackageClasses = { SimbestApplication.class, Jsr310JpaConverters.class })
@EnableAsync
public class SimbestApplication implements CommandLineRunner {

    @Autowired
    private ApplicationContext appContext;

    /**
     * @param args 默认参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SimbestApplication.class, args);
    }

    /**
     * 读取上下文初始化Bean
     *
     * @param args 默认参数
     * @throws Exception Exception
     */
    //@Override
    public void run(String... args) throws Exception {
        String[] activeProfiles = appContext.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            log.warn("Spring Boot load profile is: {}", profile);
            log.warn("Aplication started successfully, lets go and have fun......");
        }
    }
}
