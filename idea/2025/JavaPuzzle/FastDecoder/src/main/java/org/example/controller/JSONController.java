//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.example.controller;

import com.alibaba.fastjson.JSON;
import java.nio.charset.Charset;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONController {
    @PostMapping(
            value = {"/json"},
            consumes = {"application/json"})
    public Object vuln(@RequestBody String json) {
        try {
            Object parse = JSON.parse(json);
            return parse;
        } catch (Exception e) {
            Charset cs = Charset.forName("GBK");
            String err = new String("err..".getBytes(), cs);
            return err + ":\n" + e.getMessage();
        }
    }
}
