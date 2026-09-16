package com.htv.patterns.behavioral.strategy.core;

/**
 * Strategy for converting a notification request into a
 * channel-specific message.
 */
public interface NotificationStrategy {

    NotificationChannel supports();

    NotificationMessage execute(
            NotificationRequest request
    );
}