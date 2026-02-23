package com.butler.springboot14shiro.MyController;

import com.butler.springboot14shiro.Util.MyObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
/* loaded from: easyjava.jar:BOOT-INF/classes/com/butler/springboot14shiro/MyController/HelloController.class */
public class HelloController {
    @RequestMapping({"/"})
    public String index(Model model) {
        model.addAttribute("msg", "Hello World");
        return "login";
    }

    @RequestMapping({"/login"})
    public String login(String username, String password, Model model) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return "admin/hello";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        } catch (UnknownAccountException e2) {
            model.addAttribute("msg", "用户名错误");
            return "login";
        }
    }

    @RequestMapping({"/admin/hello"})
    public String admin(@RequestParam(name = "data", required = false) String data, Model model) throws Exception {
        try {
            byte[] decode = Base64.getDecoder().decode(data);
            InputStream inputStream = new ByteArrayInputStream(decode);
            MyObjectInputStream myObjectInputStream = new MyObjectInputStream(inputStream);
            myObjectInputStream.readObject();
            return "admin/hello";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg", "data=");
            return "admin/hello";
        }
    }
}
