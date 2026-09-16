package com.htv.patterns.creational.factory.abstractfactory.email;

import com.htv.patterns.creational.factory.notification.NotificationRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailAddressValidatorTest {

    private final EmailAddressValidator validator =
            new EmailAddressValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "developer@example.com",
            "ha.trong@example.vn",
            "user+pattern@example.org"
    })
    void shouldAcceptSupportedEmailAddress(
            String email
    ) {
        NotificationRequest request =
                new NotificationRequest(
                        email,
                        "Subject",
                        "Content"
                );

        assertThatCode(
                () -> validator.validate(request)
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "invalid",
            "invalid@",
            "@example.com",
            "user example.com"
    })
    void shouldRejectInvalidEmailAddress(
            String email
    ) {
        NotificationRequest request =
                new NotificationRequest(
                        email,
                        "Subject",
                        "Content"
                );

        assertThatThrownBy(
                () -> validator.validate(request)
        )
                .isInstanceOf(
                        IllegalArgumentException.class
                )
                .hasMessageContaining(
                        "Invalid email address"
                );
    }
}