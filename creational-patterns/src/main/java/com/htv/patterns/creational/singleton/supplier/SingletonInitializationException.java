package com.htv.patterns.creational.singleton.supplier;

public class SingletonInitializationException extends RuntimeException {
    public SingletonInitializationException(String message) {
        super(message);
    }

    public SingletonInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
