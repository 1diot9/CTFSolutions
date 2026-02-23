package com.example.javaguide.controller;

import com.example.javaguide.MyObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.InvalidClassException;
import java.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

@Controller
/* loaded from: javaGuide-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/javaguide/controller/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    @ResponseBody
    public String index() {
        return "hello";
    }

    @RequestMapping({"/deser"})
    @ResponseBody
    public String deserialize(@RequestParam String payload) {
        byte[] decode = Base64.getDecoder().decode(payload);
        try {
            MyObjectInputStream myObjectInputStream = new MyObjectInputStream(new ByteArrayInputStream(decode));
            myObjectInputStream.readObject();
            return "ok";
        } catch (InvalidClassException e) {
            return e.getMessage();
        } catch (Exception e2) {
            e2.printStackTrace();
            return SimpleMappingExceptionResolver.DEFAULT_EXCEPTION_ATTRIBUTE;
        }
    }
}
