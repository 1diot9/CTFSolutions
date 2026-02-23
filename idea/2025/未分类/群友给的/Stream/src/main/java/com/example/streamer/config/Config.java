package com.example.streamer.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
/* loaded from: app.jar:BOOT-INF/classes/com/example/streamer/config/Config.class */
public class Config implements WebMvcConfigurer {
    /* JADX WARN: Multi-variable type inference failed */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        ((HttpSecurity) http.csrf().disable()).authorizeRequests().regexMatchers("/file").access("hasRole('USER')").regexMatchers("/read").access("hasRole('USER')").regexMatchers(".*").permitAll();
        return http.build();
    }

    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/", "file:./static-files/");
    }

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return factory -> {
            factory.setPort(9099);
        };
    }
}