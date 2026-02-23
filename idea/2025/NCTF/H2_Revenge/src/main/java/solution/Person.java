package solution;

import java.io.IOException;
import java.io.Serializable;

public class Person implements Serializable {
    public String name;
    protected int age;
    private String gender;

    public Person() {
    }

    @Override
    public String toString() {
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Person{}";
    }

    public String getName() {
        System.out.println("getName");
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        System.out.println("getAge");
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        System.out.println("getGender");
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
