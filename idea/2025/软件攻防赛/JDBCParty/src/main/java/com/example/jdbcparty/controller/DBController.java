package com.example.jdbcparty.controller;

import com.example.jdbcparty.Utils;
import com.example.jdbcparty.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
/* loaded from: JDBCParty-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/jdbcparty/controller/DBController.class */
public class DBController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.driver}")
    private String driverClassName;

    @GetMapping({"/"})
    public String index() {
        return BeanDefinitionParserDelegate.INDEX_ATTRIBUTE;
    }

    @PostMapping({"/dbtest"})
    public ResponseEntity<String> dbtest(String data) {
        try {
            User credentials = (User) Utils.deserialize(data);
            Class.forName(this.driverClassName);
            Connection connection = DriverManager.getConnection(this.url, credentials.getUsername(), credentials.getPassword());
            try {
                if (connection.isValid(5)) {
                    ResponseEntity<String> ok = ResponseEntity.ok("connect success");
                    if (connection != null) {
                        connection.close();
                    }
                    return ok;
                }
                ResponseEntity<String> body = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("connect failed");
                if (connection != null) {
                    connection.close();
                }
                return body;
            } finally {
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("connect failed " + e.getMessage());
        }
    }
}
