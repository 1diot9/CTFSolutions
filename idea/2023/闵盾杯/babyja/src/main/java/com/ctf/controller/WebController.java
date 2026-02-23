package com.ctf.controller;

import com.alibaba.fastjson.JSON;
import com.ctf.utils.SecurityCheck;
import com.ctf.utils.Tools;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
/* loaded from: babyja.jar:BOOT-INF/classes/com/ctf/controller/WebController.class */
public class WebController {
    @GetMapping({"/"})
    @ResponseBody
    public String index() {
        return "welcome";
    }

    @RequestMapping({"/admin/{name}"})
    @ResponseBody
    public String admin(@PathVariable String name, @RequestParam(name = "data", required = true) String data) throws Exception {
        String s = new String(Tools.base64Decode(data), StandardCharsets.UTF_8);
        try {
            new SecurityCheck(s);
            JSON.parse(s);
            return "WellDone";
        } catch (SecurityException e) {
            return "hacker " + name;
        }
    }
}
