package com.example.ezjav.solution;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XString;

import java.util.HashMap;

public class Draft {
    public static void main(String[] args) throws Exception {

        HashMap<Object, Object> hashMap1 = new HashMap<>();
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        Person person = new Person();
        XString xString1 = new XString("xx");

        hashMap1.put("aa", person);
//        hashMap1.put("bb", xString1);
        hashMap2.put("aa", xString1);
//        hashMap2.put("bb", person);

        XString xString = new XString("xxx");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy", person);
        map1.put("zZ", xString);
        map2.put("yy", xString);
        map2.put("zZ", person);

        HashMap<Object, Object> hashMap = Tools.makeMap(hashMap1, hashMap2);
//        HashMap<Object, Object> hashMap = Tools.makeMap(map1, map2);
        byte[] ser = Tools.ser(hashMap);
        Tools.deser(ser);

        TemplatesImpl templates = new TemplatesImpl();

    }
}
