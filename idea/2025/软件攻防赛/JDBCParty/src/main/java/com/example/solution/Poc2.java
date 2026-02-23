package com.example.solution;

import java.lang.reflect.Method;

public class Poc2 {
    public static void main(String[] args) throws NoSuchMethodException {
        Person person = new Person("aaa");
        Method method = person.getClass().getDeclaredMethod("test");
        Object proxy = GadgetUtils17.SpringAOP(person, method);
        proxy.toString();
    }
}
