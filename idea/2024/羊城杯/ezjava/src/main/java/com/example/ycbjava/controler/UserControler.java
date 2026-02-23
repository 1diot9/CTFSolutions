package com.example.ycbjava.controler;

import com.example.ycbjava.utils.MyObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
/* loaded from: ycbjava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/ycbjava/controler/UserControler.class */
public class UserControler {
    @RequestMapping({"/user/index"})
    public String index() {
        return BeanDefinitionParserDelegate.INDEX_ATTRIBUTE;
    }

    @PostMapping({"/user/ser"})
    @ResponseBody
    public String ser(@RequestParam("ser") String ser) throws IOException, ClassNotFoundException {
        byte[] decode = Base64.getDecoder().decode(ser);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(decode);
        MyObjectInputStream objectInputStream = new MyObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        objectInputStream.readObject();
        return "Success";
    }

    @PostMapping({"/user/upload"})
    @ResponseBody
    public String handleFileUpload(MultipartFile file) {
        if (file.isEmpty()) {
            return "File upload failed";
        }
        try {
            String fileName = file.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            if (fileName.contains("../") || fileName.contains("..\\")) {
                return "File upload failed";
            }
            String suffix = fileName.substring(index);
            if (suffix.equals(".jsp")) {
                return "File upload failed";
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("D:\\tmp\\" + fileName, new String[0]);
            Files.write(path, bytes, new OpenOption[0]);
            return "File upload success";
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed";
        }
    }
}
