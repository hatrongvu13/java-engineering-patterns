package com.htv.patterns.behavioral.observer.core;

@FunctionalInterface
public interface EventListenerSubscription {

    void unsubscribe();
}