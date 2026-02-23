package com.example.jdbcparty.model;

import java.io.Serializable;

/* loaded from: JDBCParty-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/jdbcparty/model/User.class */
public class User implements Serializable {
    private String username;
    private String password;

    public User(String password, String username) {
        this.password = password;
        this.username = username;
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

    public String getInfo() {
        return "User{username='" + this.username + "', password='" + this.password + "'}";
    }
}
