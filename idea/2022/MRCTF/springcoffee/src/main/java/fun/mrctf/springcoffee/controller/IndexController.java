package fun.mrctf.springcoffee.controller;

import fun.mrctf.springcoffee.model.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: springcoffee-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/fun/mrctf/springcoffee/controller/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    public Message index() {
        return new Message(200, "There is no flag but a cup of coffee");
    }
}
