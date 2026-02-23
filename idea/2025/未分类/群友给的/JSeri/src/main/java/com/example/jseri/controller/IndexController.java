package com.example.jseri.controller;

import com.example.jseri.entity.DataVo;
import com.informix.jdbc.IfxDriver;
import java.io.IOException;
import java.sql.DriverManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: app(1).jar:BOOT-INF/classes/com/example/jseri/controller/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    public String index() {
        return "Hello World!";
    }

    @RequestMapping({"/test"})
    public String test(@RequestBody DataVo data) throws IOException, ClassNotFoundException {
        try {
            if (!data.getType().isEmpty() && data.getType().equals("informix") && !data.getUrl().isEmpty()) {
                DriverManager.registerDriver(new IfxDriver());
                DriverManager.getConnection(data.getUrl());
                return "Test Success!";
            }
            return "Test Fail!";
        } catch (Exception e) {
            return "Test Failed!";
        }
    }
}