package com.htv.patterns.creational.factory.abstractfactory;

import com.htv.patterns.creational.factory.abstractfactory.core.NotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationFormatter;
import com.htv.patterns.creational.factory.abstractfactory.core.NotificationValidator;
import com.htv.patterns.creational.factory.abstractfactory.email.EmailAddressValidator;
import com.htv.patterns.creational.factory.abstractfactory.email.EmailNotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.email.HtmlEmailFormatter;
import com.htv.patterns.creational.factory.abstractfactory.push.DeviceTokenValidator;
import com.htv.patterns.creational.factory.abstractfactory.push.PushMessageFormatter;
import com.htv.patterns.creational.factory.abstractfactory.push.PushNotificationComponentFactory;
import com.htv.patterns.creational.factory.abstractfactory.sms.PhoneNumberValidator;
import com.htv.patterns.creational.factory.abstractfactory.sms.PlainTextSmsFormatter;
import com.htv.patterns.creational.factory.abstractfactory.sms.SmsNotificationComponentFactory;
import com.htv.patterns.creational.factory.notification.EmailNotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationSender;
import com.htv.patterns.creational.factory.notification.NotificationType;
import com.htv.patterns.creational.factory.notification.PushNotificationSender;
import com.htv.patterns.creational.factory.notification.SmsNotificationSender;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractNotificationFactoryTest {

    @ParameterizedTest
    @MethodSource("factoryCases")
    void shouldCreateCompatibleProductFamily(
            NotificationComponentFactory factory,
            NotificationType expectedType,
            Class<? extends NotificationValidator>
                    validatorType,
            Class<? extends NotificationFormatter>
                    formatterType,
            Class<? extends NotificationSender>
                    senderType
    ) {
        NotificationValidator validator =
                factory.createValidator();

        NotificationFormatter formatter =
                factory.createFormatter();

        NotificationSender sender =
                factory.createSender();

        assertThat(factory.supports())
                .isEqualTo(expectedType);

        assertThat(validator)
                .isInstanceOf(validatorType);

        assertThat(formatter)
                .isInstanceOf(formatterType);

        assertThat(sender)
                .isInstanceOf(senderType);

        assertThat(validator.supports())
                .isEqualTo(expectedType);

        assertThat(formatter.supports())
                .isEqualTo(expectedType);

        assertThat(sender.supports())
                .isEqualTo(expectedType);
    }

    private static Stream<Arguments>
    factoryCases() {
        return Stream.of(
                Arguments.of(
                        new EmailNotificationComponentFactory(),
                        NotificationType.EMAIL,
                        EmailAddressValidator.class,
                        HtmlEmailFormatter.class,
                        EmailNotificationSender.class
                ),
                Arguments.of(
                        new SmsNotificationComponentFactory(),
                        NotificationType.SMS,
                        PhoneNumberValidator.class,
                        PlainTextSmsFormatter.class,
                        SmsNotificationSender.class
                ),
                Arguments.of(
                        new PushNotificationComponentFactory(),
                        NotificationType.PUSH,
                        DeviceTokenValidator.class,
                        PushMessageFormatter.class,
                        PushNotificationSender.class
                )
        );
    }
}