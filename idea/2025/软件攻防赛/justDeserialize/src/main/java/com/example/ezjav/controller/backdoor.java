package com.example.ezjav.controller;

import com.example.ezjav.utils.MyObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: justDeserialize-1.0-SNAPSHOT.jar:BOOT-INF/classes/com/example/ezjav/controller/backdoor.class */
public class backdoor {
    static String banner = "Welcome to java";

    @RequestMapping({"/"})
    public String index() throws Exception {
        return banner;
    }

    @RequestMapping({"/read"})
    public String read(@RequestBody String body) {
        if (body != null) {
            try {
                byte[] data = Base64.getDecoder().decode(body);
                String temp = new String(data);
                if (temp.contains("naming") || temp.contains("com.sun") || temp.contains("jdk.jfr")) {
                    return "banned";
                }
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                MyObjectInputStream objectInputStream = new MyObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                return object.getClass().toString();
            } catch (Exception e) {
                return e.toString();
            }
        }
        return "ok";
    }
}
