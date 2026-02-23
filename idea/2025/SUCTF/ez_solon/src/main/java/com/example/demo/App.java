package com.example.demo;

import com.example.demo.security.RestrictiveSecurityManager;
import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
/* loaded from: ez-solon.jar:BOOT-INF/classes/com/example/demo/App.class */
public class App {
    public static void main(String[] args) {
        Solon.start((Class<?>) App.class, args);
        try {
            System.setSecurityManager(new RestrictiveSecurityManager());
        } catch (SecurityException e) {
            System.err.println("无法设置 SecurityManager: " + e.getMessage());
        }
    }
}
