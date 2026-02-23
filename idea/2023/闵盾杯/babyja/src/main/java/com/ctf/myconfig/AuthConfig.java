package com.ctf.myconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
/* loaded from: babyja.jar:BOOT-INF/classes/com/ctf/myconfig/AuthConfig.class */
public class AuthConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> {
            authorize.regexMatchers("/admin/.*").authenticated();
        }).httpBasic(Customizer.withDefaults()).formLogin(Customizer.withDefaults()).csrf().disable();
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder().username("xxxxxxxxxxxx").password("xxxxxxxxxxxx").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }
}
