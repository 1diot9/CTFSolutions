package com.example.demo.controller;

import com.caucho.hessian.io.Hessian2Input;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;
import org.noear.solon.annotation.Param;

@Controller
/* loaded from: ez-solon.jar:BOOT-INF/classes/com/example/demo/controller/IndexController.class */
public class IndexController {
    @Mapping("/hello")
    public String hello(@Param(defaultValue = "hello") String data) throws Exception {
        byte[] decode = Base64.getDecoder().decode(data);
        Hessian2Input hessian2Input = new Hessian2Input(new ByteArrayInputStream(decode));
        Object object = hessian2Input.readObject();
        return object.toString();
    }
}
