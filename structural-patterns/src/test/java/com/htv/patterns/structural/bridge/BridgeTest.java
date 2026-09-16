package com.htv.patterns.structural.bridge;

import com.htv.patterns.structural.bridge.abstraction.AlertNotification;
import com.htv.patterns.structural.bridge.abstraction.Notification;
import com.htv.patterns.structural.bridge.abstraction.OtpNotification;
import com.htv.patterns.structural.bridge.implementor.EmailMessageSender;
import com.htv.patterns.structural.bridge.implementor.SmsMessageSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BridgeTest {

    @Test
    void sameAbstractionShouldWorkOverDifferentSenders() {
        Notification emailAlert =
                new AlertNotification(
                        new EmailMessageSender(),
                        "disk full"
                );

        Notification smsAlert =
                new AlertNotification(
                        new SmsMessageSender(),
                        "disk full"
                );

        assertThat(emailAlert.notify("ops@example.com"))
                .isEqualTo("email://ops@example.com | [ALERT] disk full");
        assertThat(smsAlert.notify("+123"))
                .isEqualTo("sms://+123 | [ALERT] disk full");
    }

    @Test
    void differentAbstractionsShouldReuseSameSender() {
        SmsMessageSender sms = new SmsMessageSender();

        Notification alert =
                new AlertNotification(sms, "hi");
        Notification otp =
                new OtpNotification(sms, "998877");

        assertThat(alert.channel()).isEqualTo("SMS");
        assertThat(otp.notify("+1"))
                .isEqualTo("sms://+1 | Your verification code is 998877");
    }
}
