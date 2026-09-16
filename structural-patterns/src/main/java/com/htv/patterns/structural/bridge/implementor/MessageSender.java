package com.htv.patterns.structural.bridge.implementor;

/**
 * Implementor side of the bridge: the concrete transport that
 * delivers a rendered message. Abstractions (notification types)
 * are composed with, not coupled to, a specific sender.
 */
public interface MessageSender {

    String channel();

    String send(
            String recipient,
            String payload
    );
}
