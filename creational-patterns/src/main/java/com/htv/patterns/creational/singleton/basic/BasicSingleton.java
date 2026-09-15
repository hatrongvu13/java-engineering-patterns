package com.htv.patterns.creational.singleton.basic;

public class BasicSingleton {
    private static final BasicSingleton INSTANCE = new BasicSingleton();

    private BasicSingleton() {}

    public static BasicSingleton getInstance() {
        return INSTANCE;
    }

    public String description() {
        return "Basic singleton pattern";
    }
}
