package com.example.demo;

import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping({DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL})
@Controller
/* loaded from: webauth.jar:BOOT-INF/classes/com/example/demo/Login.class */
public class Login {
    @GetMapping({"/dynamic-template"})
    public String getDynamicTemplate(@RequestParam(value = "value", required = false) String value) {
        if (value.equals("")) {
            value = "login";
        }
        return value + ".html";
    }
}
