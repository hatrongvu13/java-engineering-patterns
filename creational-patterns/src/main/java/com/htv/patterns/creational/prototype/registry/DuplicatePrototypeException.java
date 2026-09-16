package com.htv.patterns.creational.prototype.registry;

public final class DuplicatePrototypeException
        extends RuntimeException {

    public DuplicatePrototypeException(
            String key
    ) {
        super(
                "A report template prototype is already "
                        + "registered for key: "
                        + key
        );
    }
}