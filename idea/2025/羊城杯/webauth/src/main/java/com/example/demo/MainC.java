package com.example.demo;

import java.io.File;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
/* loaded from: webauth.jar:BOOT-INF/classes/com/example/demo/MainC.class */
public class MainC {
    @PostMapping({"/upload"})
    public String upload(@RequestParam("imgFile") MultipartFile file, @RequestParam("imgName") String name) throws Exception {
        File dir = new File("uploadFile");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // name=../templates/login.html
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + name + ".html"));
        return "success";
    }
}
