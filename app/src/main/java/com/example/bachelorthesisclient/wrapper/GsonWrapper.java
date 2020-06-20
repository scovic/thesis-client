package com.example.bachelorthesisclient.wrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonWrapper {
    public static String toJson(Object value) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(value);
    }

    public static Object fromJson(String json, Class<?> c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    public static Object fromJson(String json, Type t) {
        Gson gson = new Gson();
        return gson.fromJson(json, t);
    }
}
