package com.example.streamer.controller;

import com.example.streamer.utils.MyClass;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Objects;
import javax.xml.transform.TransformerException;
import org.apache.poi.xssf.extractor.XSSFExportToXml;
import org.apache.poi.xssf.usermodel.XSSFMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@RestController
/* loaded from: app.jar:BOOT-INF/classes/com/example/streamer/controller/IndexController.class */
public class IndexController {
    @RequestMapping({"/"})
    public String index() {
        return "Hello World";
    }

    @RequestMapping({"/read"})
    public String read(@RequestBody String data) throws IOException, ClassNotFoundException {
        if (!data.startsWith("rO0AB")) {
            return "data format error!";
        }
        byte[] bytes = Base64.getDecoder().decode(data);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new MyClass(byteArrayInputStream);
        objectInputStream.readObject();
        return "success!";
    }

    @RequestMapping({"/file"})
    public String file(@RequestParam("file") MultipartFile file) throws TransformerException, SAXException, IOException {
        if (file.isEmpty()) {
            return "file is empty!";
        }
        XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());
        for (XSSFMap map : wb.getCustomXMLMappings()) {
            XSSFExportToXml exporter = new XSSFExportToXml(map);
            exporter.exportToXML(System.out, true);
        }
        Objects.requireNonNull(System.out);
        return "load success!";
    }
}