package com.rois.happy_shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
/* loaded from: happy_shopping-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/rois/happy_shopping/config/SecurityConfig.class */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
    protected void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity) ((HttpSecurity) ((HttpSecurity) http.csrf().disable()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()).authorizeRequests().anyRequest().permitAll().and()).cors();
    }
}
