package com.example.ezjav.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;

/* loaded from: justDeserialize-1.0-SNAPSHOT.jar:BOOT-INF/classes/com/example/ezjav/utils/MyObjectInputStream.class */
public class MyObjectInputStream extends ObjectInputStream {
    private String[] denyClasses;

    public MyObjectInputStream(ByteArrayInputStream var1) throws IOException {
        super(var1);
        ArrayList<String> classList = new ArrayList<>();
        InputStream file = MyObjectInputStream.class.getResourceAsStream("/blacklist.txt");
        BufferedReader var2 = new BufferedReader(new InputStreamReader(file));
        while (true) {
            String var4 = var2.readLine();
            if (var4 != null) {
                classList.add(var4.trim());
            } else {
                this.denyClasses = new String[classList.size()];
                classList.toArray(this.denyClasses);
                return;
            }
        }
    }

    @Override // java.io.ObjectInputStream
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        int var5 = this.denyClasses.length;
        for (int var6 = 0; var6 < var5; var6++) {
            String denyClass = this.denyClasses[var6];
            if (className.startsWith(denyClass)) {
                throw new InvalidClassException("Unauthorized deserialization attempt", className);
            }
        }
        return super.resolveClass(desc);
    }
}
