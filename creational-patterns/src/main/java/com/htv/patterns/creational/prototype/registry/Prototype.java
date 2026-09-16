package com.htv.patterns.creational.prototype.registry;

/**
 * Contract for objects that can produce an independent copy.
 *
 * @param <T> copied object type
 */
@FunctionalInterface
public interface Prototype<T> {

    T copy();
}