package com.example.ezjav.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Comparator;

/* loaded from: justDeserialize-1.0-SNAPSHOT.jar:BOOT-INF/classes/com/example/ezjav/utils/User.class */
public class User implements Serializable, Comparator {
    public String username;
    public String password;
    public Object compare;

    public User(String user, String pass, String cmp) {
        this.username = user;
        this.password = pass;
        this.compare = cmp;
    }

    @Override // java.util.Comparator
    public int compare(Object o1, Object o2) {
        try {
            Method m = this.compare.getClass().getDeclaredMethod("compare", Object.class, Object.class);
            m.setAccessible(true);
            m.invoke(this.compare, o1, o2);
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
