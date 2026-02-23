package com.example.ycbjava.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/* loaded from: ycbjava-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/ycbjava/utils/MyObjectInputStream.class */
public class MyObjectInputStream extends ObjectInputStream {
    private static final String[] blacklist = {"java.lang.Runtime", "java.lang.ProcessBuilder", "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl", "java.security.SignedObject", "com.sun.jndi.ldap.LdapAttribute", "org.apache.commons.beanutils", "org.apache.commons.collections", "javax.management.BadAttributeValueExpException", "com.sun.org.apache.xpath.internal.objects.XString"};

    public MyObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        String[] var3 = blacklist;
        for (String forbiddenPackage : var3) {
            if (className.startsWith(forbiddenPackage)) {
                throw new InvalidClassException("Unauthorized deserialization attempt", className);
            }
        }
        return super.resolveClass(desc);
    }
}
