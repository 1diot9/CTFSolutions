package com.butler.springboot14shiro.Config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/* loaded from: easyjava.jar:BOOT-INF/classes/com/butler/springboot14shiro/Config/ShiroConfig.class */
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/", "anon");
        filterMap.put("/login", "anon");
        filterMap.put("/admin/*", "authc");
        bean.setFilterChainDefinitionMap(filterMap);
        bean.setLoginUrl("/login");
        return bean;
    }

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("adminRealm") AdminRealm adminRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(adminRealm);
        return securityManager;
    }

    @Bean
    public AdminRealm adminRealm() {
        return new AdminRealm();
    }
}
