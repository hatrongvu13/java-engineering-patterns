package com.htv.patterns.core.result;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResultTest {

    @Test
    void shouldCreateSuccessCarryingValue() {
        Result<String> result =
                Result.success("value");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isFailure()).isFalse();
        assertThat(result.value()).isEqualTo("value");
    }

    @Test
    void shouldCreateFailureCarryingCause() {
        Result<String> result =
                Result.failure("boom");

        assertThat(result.isFailure()).isTrue();
        assertThat(result.cause())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("boom");
    }

    @Test
    void shouldRejectNullSuccessValue() {
        assertThatThrownBy(
                () -> Result.success(null)
        )
                .isInstanceOf(NullPointerException.class)
                .hasMessage("success value must not be null");
    }

    @Test
    void shouldCaptureThrownExceptionInOf() {
        Result<String> result =
                Result.of(() -> {
                    throw new IllegalArgumentException("bad");
                });

        assertThat(result.isFailure()).isTrue();
        assertThat(result.cause())
                .hasMessage("bad");
    }

    @Test
    void shouldMapSuccessValue() {
        Result<Integer> result =
                Result.success("abc")
                        .map(String::length);

        assertThat(result.value()).isEqualTo(3);
    }

    @Test
    void shouldPropagateFailureThroughMap() {
        Result<Integer> result =
                Result.<String>failure("x")
                        .map(String::length);

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    void shouldFlatMapSuccessValue() {
        Result<Integer> result =
                Result.success("abc")
                        .flatMap(v -> Result.success(v.length()));

        assertThat(result.value()).isEqualTo(3);
    }

    @Test
    void shouldReturnOtherOnOrElseForFailure() {
        String value =
                Result.<String>failure("x")
                        .orElse("fallback");

        assertThat(value).isEqualTo("fallback");
    }

    @Test
    void shouldRunOnSuccessHookOnly() {
        AtomicReference<String> captured =
                new AtomicReference<>();

        Result.success("hit")
                .onSuccess(captured::set)
                .onFailure(cause -> captured.set("miss"));

        assertThat(captured.get()).isEqualTo("hit");
    }

    @Test
    void shouldRunOnFailureHookOnly() {
        AtomicReference<String> captured =
                new AtomicReference<>();

        Result.<String>failure("err")
                .onSuccess(captured::set)
                .onFailure(cause -> captured.set(cause.getMessage()));

        assertThat(captured.get()).isEqualTo("err");
    }

    @Test
    void shouldThrowWhenReadingValueFromFailure() {
        assertThatThrownBy(
                () -> Result.failure("x").value()
        )
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldConvertToOptional() {
        assertThat(Result.success("v").toOptional())
                .contains("v");

        assertThat(Result.failure("x").toOptional())
                .isEmpty();
    }
}
