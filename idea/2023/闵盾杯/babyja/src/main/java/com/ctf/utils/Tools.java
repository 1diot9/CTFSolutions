package com.ctf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/* loaded from: babyja.jar:BOOT-INF/classes/com/ctf/utils/Tools.class */
public class Tools {
    private Tools() {
    }

    public static void deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }

    public static byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] base64Decode(String string) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(string);
    }

    public static String base64Encode(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] encoded = Base64.getEncoder().encode(bytes);
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
