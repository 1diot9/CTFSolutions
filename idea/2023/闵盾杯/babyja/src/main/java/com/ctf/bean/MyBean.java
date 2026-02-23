package com.ctf.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* loaded from: babyja.jar:BOOT-INF/classes/com/ctf/bean/MyBean.class */
public class MyBean implements Serializable, Data {
    public String database;
    public String host;
    public String username;
    public String password;
    public Connection connection;

    @Override // com.ctf.bean.Data
    public Connection getConnection() {
        String url = "jdbc:" + this.database + "://" + this.host + ":9999/test?user=" + this.username + "&password=" + this.password;
        try {
            this.connection = DriverManager.getConnection(url);
            return this.connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setHots(String host) {
        this.host = host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
