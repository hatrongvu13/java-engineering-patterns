package com.htv.patterns.creational.prototype.registry;

public final class PrototypeNotFoundException
        extends RuntimeException {

    public PrototypeNotFoundException(
            String key
    ) {
        super(
                "No report template prototype is "
                        + "registered for key: "
                        + key
        );
    }
}