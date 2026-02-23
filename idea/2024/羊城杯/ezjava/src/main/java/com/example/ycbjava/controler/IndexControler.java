package com.example.ycbjava.controler;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: ycbjava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/ycbjava/controler/IndexControler.class */
public class IndexControler {
    @RequestMapping({"/doLogin"})
    public String createSql(@RequestParam("username") String username, @RequestParam("password") String password) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return "forward:/user/index";
        } catch (AuthenticationException e) {
            return "forward:/login";
        }
    }

    @RequestMapping({"/wrong"})
    @ResponseBody
    public String error() {
        return "wrong";
    }

    @RequestMapping({"/login"})
    public String login() {
        return "login";
    }
}
