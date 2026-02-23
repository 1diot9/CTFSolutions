package com.example.ycbjava.config;

import ch.qos.logback.classic.ClassicConstants;
import java.util.LinkedHashMap;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/* loaded from: ycbjava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/ycbjava/config/ShiroConfig.class */
public class ShiroConfig {
    @Bean
    MainRealm mainRealm() {
        return new MainRealm();
    }

    @Bean
    SecurityManager securityManager(MainRealm mainRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRememberMeManager(null);
        manager.setRealm(mainRealm);
        return manager;
    }

    @Bean(name = {"shiroFilter"})
    ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login");
        bean.setUnauthorizedUrl("/wrong");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/doLogin", "anon");
        map.put("/user/*", ClassicConstants.USER_MDC_KEY);
        map.put("/*", ClassicConstants.USER_MDC_KEY);
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }
}
