package com.example.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
/* loaded from: webauth.jar:BOOT-INF/classes/com/example/demo/InMemoryUserDetailsService.class */
public class InMemoryUserDetailsService implements UserDetailsService {
    private static final Map<String, User> USERS = new HashMap();

    static {
        USERS.put("user1", new User("user1", "{noop}password1", true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
        USERS.put("admin", new User("admin", "{noop}adminpass", true, true, true, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))));
    }

    @Override // org.springframework.security.core.userdetails.UserDetailsService
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = USERS.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }
}
