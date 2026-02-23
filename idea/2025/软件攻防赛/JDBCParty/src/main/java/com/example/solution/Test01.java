package com.example.solution;

import com.alibaba.fastjson2.JSONArray;

public class Test01 {
    public static void main(String[] args) {
        JSONArray jsonArray = new JSONArray();
        Person person = new Person("aaa");
        jsonArray.add(person);
        jsonArray.toString();
    }
}
