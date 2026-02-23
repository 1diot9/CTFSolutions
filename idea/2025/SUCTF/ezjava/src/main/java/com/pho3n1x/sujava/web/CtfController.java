package com.pho3n1x.sujava.web;

import ch.qos.logback.classic.spi.CallerData;
import com.pho3n1x.sujava.security.SecurityChecker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: suJava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/pho3n1x/sujava/web/CtfController.class */
public class CtfController {
    public static String MYSQL_CONNECT_URL = "jdbc:mysql://%s:%s/%s";

    @RequestMapping({"/"})
    @ResponseBody
    public String hello() {
        return "this is a suJava\ntry to hack me by /jdbc";
    }

    @GetMapping({"/jdbc"})
    public String jdbc_show() {
        return "index.html";
    }

    @PostMapping({"/jdbc"})
    @ResponseBody
    public String jdbc(@RequestParam(name = "host") String host, @RequestParam(name = "port") Integer port, @RequestParam(name = "database") String database, @RequestParam(name = "extraParams") String extraParams, @RequestParam(name = "username") String username, @RequestParam(name = "password") String password) throws Exception {
        Map<String, Object> extraparams = jsonToMap(extraParams);
        SecurityChecker.checkJdbcConnParams(host, port, username, password, database, extraparams);
        String url = String.format(MYSQL_CONNECT_URL, host.trim(), port, database.trim());
        SecurityChecker.appendMysqlForceParams(extraparams);
        String extraParamString = (String) extraparams.entrySet().stream().map(e -> {
            return String.join("=", (CharSequence) e.getKey(), String.valueOf(e.getValue()));
        }).collect(Collectors.joining(BeanFactory.FACTORY_BEAN_PREFIX));
        Connection connection = DriverManager.getConnection(url + CallerData.NA + extraParamString);
        connection.close();
        return "Success";
    }

    public static Map<String, Object> jsonToMap(String jsonString) {
        Map<String, Object> map = new HashMap<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean isKey = true;
        boolean isString = false;
        for (char c : jsonString.toCharArray()) {
            if (c == '{' || c == '[') {
                stack.push(Character.valueOf(c));
            } else if (c == '}' || c == ']') {
                stack.pop();
            } else if (c == '\"') {
                isString = !isString;
            } else if (c == ':' && !isString) {
                isKey = false;
            } else if (c == ',' && !isString && stack.size() == 1) {
                map.put(key.toString().trim(), value.toString().trim());
                key.setLength(0);
                value.setLength(0);
                isKey = true;
            } else if (isKey) {
                key.append(c);
            } else {
                value.append(c);
            }
        }
        if (key.length() > 0 && value.length() > 0) {
            map.put(key.toString().trim(), value.toString().trim());
        }
        return map;
    }
}
