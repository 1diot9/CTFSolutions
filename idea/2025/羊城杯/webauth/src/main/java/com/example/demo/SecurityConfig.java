package com.example.demo;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
/* loaded from: webauth.jar:BOOT-INF/classes/com/example/demo/SecurityConfig.class */
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsService();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        ((HttpSecurity) http.csrf().disable()).authorizeHttpRequests(authz -> {
            authz.requestMatchers("/upload").hasRole("USER").requestMatchers("/").hasRole("USER").anyRequest().permitAll();
        }).addFilterBefore((Filter) new JwtAuthenticationFilter(this.jwtTokenProvider, userDetailsService()), UsernamePasswordAuthenticationFilter.class).formLogin(form -> {
            form.loginPage("/login/dynamic-template?value=login").permitAll();
        });
        return http.build();
    }
}
