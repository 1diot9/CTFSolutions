package org.example.demo.controllers;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: demo-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/demo/controllers/Index.class */
public class Index {
    @GetMapping({"/"})
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping({"/source_tr15d0"})
    public String source() {
        return "@GetMapping(\"/lookup\")\npublic String lookup(String path) {\n    try {\n        String url = \"ldap://\" + path;\n        InitialContext initialContext = new InitialContext();\n        initialContext.lookup(url);\n        return \"ok\";\n    }catch (NamingException e){\n        return \"failed\";\n    }\n}\n";
    }

    @GetMapping({"/lookup"})
    public String lookup(String path) {
        try {
            String url = "ldap://" + path;
            InitialContext initialContext = new InitialContext();
            initialContext.lookup(url);
            return "ok";
        } catch (NamingException e) {
            e.printStackTrace();
            return "failed";
        }
    }
}
