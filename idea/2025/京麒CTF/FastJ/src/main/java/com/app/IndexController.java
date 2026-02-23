package com.app;

import com.alibaba.fastjson.JSON;
import java.io.FileNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: FastJ-1.0-SNAPSHOT-11.jar:BOOT-INF/classes/com/app/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    public Object fastj(String json) {
        if (json == null) {
            return JSON.toJSONString("json is null");
        }
        try {
            return JSON.parse(json);
        } catch (Exception e) {
            return e.toString();
        }
    }

    private void getflag() throws FileNotFoundException {
        new FilterFileOutputStream("/flag", "/");
    }
}
