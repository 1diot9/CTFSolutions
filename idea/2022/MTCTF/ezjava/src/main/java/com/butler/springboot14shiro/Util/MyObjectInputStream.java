package com.butler.springboot14shiro.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: easyjava.jar:BOOT-INF/classes/com/butler/springboot14shiro/Util/MyObjectInputStream.class */
public class MyObjectInputStream extends ObjectInputStream {
    private static ArrayList<String> blackList = new ArrayList<>();

    static {
        blackList.add("com.sun.org.apache.xalan.internal.xsltc.traxTemplatesImpl");
        blackList.add("org.hibernate.tuple.component.PojoComponentTuplizer");
        blackList.add("java.security.SignedObject");
        blackList.add("com.sun.rowset.JdbcRowSetImpl");
    }

    public MyObjectInputStream(InputStream inputStream) throws Exception {
        super(inputStream);
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        Iterator<String> it = blackList.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (desc.getName().contains(s)) {
                throw new ClassNotFoundException("Don't hacker!");
            }
        }
        return super.resolveClass(desc);
    }
}
