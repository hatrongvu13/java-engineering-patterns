package com.htv.patterns.behavioral.strategy.registry;

import com.htv.patterns.behavioral.strategy.basic.EmailNotificationStrategy;
import com.htv.patterns.behavioral.strategy.basic.PushNotificationStrategy;
import com.htv.patterns.behavioral.strategy.basic.SmsNotificationStrategy;

public final class DefaultNotificationStrategyRegistry {

    private DefaultNotificationStrategyRegistry() {
        throw new AssertionError(
                "Factory class must not be instantiated"
        );
    }

    public static NotificationStrategyRegistry create() {
        return new NotificationStrategyRegistryBuilder()
                .register(
                        new EmailNotificationStrategy()
                )
                .register(
                        new SmsNotificationStrategy()
                )
                .register(
                        new PushNotificationStrategy()
                )
                .build();
    }
}