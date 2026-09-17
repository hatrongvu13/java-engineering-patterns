package com.htv.patterns.integration.core;

@FunctionalInterface
public interface MessageHandler<T> {
    void handle(Message<T> message);
}
