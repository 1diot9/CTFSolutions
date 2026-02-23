package com.example.ycbjava.bean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/* loaded from: ycbjava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/ycbjava/bean/User.class */
public class User implements Serializable {
    public String username;
    public String password;
    public String gift;

    public User() {
        this.username = "admin";
        this.password = "admin888";
    }

    public String getGift() {
        String gift = this.username.trim().toLowerCase();
        if (gift.startsWith("http") || gift.startsWith("file")) {
            gift = "nonono";
        }
        try {
            URL url1 = new URL(gift);
            Class<?> URLclass = Class.forName("java.net.URLClassLoader");
            Method add = URLclass.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            add.invoke(classloader, url1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public User(String username, String password) {
        this.username = "admin";
        this.password = "admin888";
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
