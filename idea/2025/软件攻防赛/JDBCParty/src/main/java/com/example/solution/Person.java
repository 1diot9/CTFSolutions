package com.example.solution;

import java.io.IOException;
import java.io.Serializable;

public class Person implements Serializable {
    public String name;
    protected int age;
    private String address;

    public Person(String name) {
        this.name = name;
    }

    public static void test(){
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        System.out.println("getName");
        return name;
    }

    public void setName(String name) {
        System.out.println("setName");
        this.name = name;
    }

    public int getAge() {
        System.out.println("getAge");
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        System.out.println("getAddress");
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
