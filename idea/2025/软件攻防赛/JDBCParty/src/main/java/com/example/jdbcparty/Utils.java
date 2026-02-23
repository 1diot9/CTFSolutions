package com.example.jdbcparty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/* loaded from: JDBCParty-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/com/example/jdbcparty/Utils.class */
public class Utils {
    public static String serialize(Object obj) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(obj);
        String payload = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        System.out.println(payload);
        return payload;
    }

    public static Object deserialize(String payload) throws Exception {
        byte[] data = Base64.getDecoder().decode(payload);
        return new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
    }
}
