package com.example.derby;

import javax.naming.InitialContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: Derby.jar:BOOT-INF/classes/com/example/derby/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    public String index() {
        return "hello derby";
    }

    @RequestMapping({"/lookup"})
    public String lookup(@RequestParam String url) throws Exception {
        new InitialContext().lookup(url);
        return "ok";
    }
}
