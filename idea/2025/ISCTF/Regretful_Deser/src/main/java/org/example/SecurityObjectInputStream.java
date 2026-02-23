//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class SecurityObjectInputStream extends ObjectInputStream {
    public static String[] blacklist = new String[]{"org.apache.commons.collections", "javax.swing", "com.sun.rowset", "com.sun.org.apache.xalan", "java.security", "java.rmi.MarshalledObject", "javax.management.remote.rmi.RMIConnector"};

    public SecurityObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    protected Class resolveClass(ObjectStreamClass cls) throws IOException, ClassNotFoundException {
        if (!contains(cls.getName())) {
            return super.resolveClass(cls);
        } else {
            System.out.println("Unexpected serialized class" + cls.getName());
            throw new InvalidClassException("Unexpected serialized class", cls.getName());
        }
    }

    public static boolean contains(String targetValue) {
        for(String forbiddenPackage : blacklist) {
            if (targetValue.contains(forbiddenPackage)) {
                return true;
            }
        }

        return false;
    }
}
