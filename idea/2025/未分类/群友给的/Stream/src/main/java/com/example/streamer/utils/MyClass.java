package com.example.streamer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/* loaded from: app.jar:BOOT-INF/classes/com/example/streamer/utils/MyClass.class */
public class MyClass extends ObjectInputStream {
    public MyClass(InputStream in) throws IOException {
        super(in);
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        if (desc.getName().equals("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl")) {
            return null;
        }
        return super.resolveClass(desc);
    }
}