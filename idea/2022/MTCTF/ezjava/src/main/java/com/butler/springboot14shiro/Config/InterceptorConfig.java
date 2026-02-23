package com.butler.springboot14shiro.Config;

import com.butler.springboot14shiro.Interceptor.evilInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] VulPathPatterns = {"/test"};

        registry.addInterceptor(new evilInterceptor()).addPathPatterns(VulPathPatterns);
    }
}
