package com.example.babyjxvx.FlagController;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.scxml2.SCXMLExecutor;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.SCXML;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Controller
/* loaded from: BabyJxVx.jar:BOOT-INF/classes/com/example/babyjxvx/FlagController/Flagcontroller.class */
public class Flagcontroller {
    private static Boolean check(String fileName) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(fileName);
        int node1 = doc.getElementsByTagName("script").getLength();
        int node2 = doc.getElementsByTagName("datamodel").getLength();
        int node3 = doc.getElementsByTagName("invoke").getLength();
        int node4 = doc.getElementsByTagName("param").getLength();
        int node5 = doc.getElementsByTagName("parallel").getLength();
        int node6 = doc.getElementsByTagName("history").getLength();
        int node7 = doc.getElementsByTagName("transition").getLength();
        int node8 = doc.getElementsByTagName("state").getLength();
        int node9 = doc.getElementsByTagName("onentry").getLength();
        int node10 = doc.getElementsByTagName("if").getLength();
        int node11 = doc.getElementsByTagName("elseif").getLength();
        if (node1 > 0 || node2 > 0 || node3 > 0 || node4 > 0 || node5 > 0 || node6 > 0 || node7 > 0 || node8 > 0 || node9 > 0 || node10 > 0 || node11 > 0) {
            return false;
        }
        return true;
    }

    @RequestMapping({"/"})
    public String index() {
        return BeanDefinitionParserDelegate.INDEX_ATTRIBUTE;
    }

    @RequestMapping({"/Flag"})
    @ResponseBody
    public String Flag(@RequestParam(required = true) String filename) {
        SCXMLExecutor executor = new SCXMLExecutor();
        try {
            if (check(filename).booleanValue()) {
                SCXML scxml = SCXMLReader.read(filename);
                executor.setStateMachine(scxml);
                executor.go();
                return "Revenge to me!";
            }
            System.out.println("nonono");
            return "revenge?";
        } catch (Exception var5) {
            System.out.println(var5);
            return "revenge?";
        }
    }
}
