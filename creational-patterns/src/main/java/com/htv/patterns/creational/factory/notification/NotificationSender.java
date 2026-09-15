package com.htv.patterns.creational.factory.notification;

public interface NotificationSender {

    NotificationType supports();

    NotificationResult send(
            NotificationRequest request
    );
}