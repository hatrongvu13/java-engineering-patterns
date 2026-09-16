package com.htv.patterns.core.result;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Immutable functional result carrying either a success value
 * or a failure cause, without throwing across boundaries.
 *
 * <p>Prefer this over returning {@code null} or leaking checked
 * exceptions from a pattern's public API.
 *
 * @param <T> the success value type
 */
public sealed interface Result<T>
        permits Result.Success, Result.Failure {

    static <T> Result<T> success(
            T value
    ) {
        return new Success<>(value);
    }

    static <T> Result<T> failure(
            Throwable cause
    ) {
        return new Failure<>(cause);
    }

    static <T> Result<T> failure(
            String message
    ) {
        return new Failure<>(
                new IllegalStateException(message)
        );
    }

    /**
     * Runs {@code supplier}, wrapping any thrown exception into a
     * {@link Failure} instead of propagating it.
     */
    static <T> Result<T> of(
            Supplier<? extends T> supplier
    ) {
        Objects.requireNonNull(
                supplier,
                "supplier must not be null"
        );

        try {
            return success(supplier.get());
        } catch (RuntimeException exception) {
            return failure(exception);
        }
    }

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    /**
     * @return the success value
     * @throws NoSuchElementException if this is a failure
     */
    T value();

    /**
     * @return the failure cause
     * @throws NoSuchElementException if this is a success
     */
    Throwable cause();

    <R> Result<R> map(
            Function<? super T, ? extends R> mapper
    );

    <R> Result<R> flatMap(
            Function<? super T, Result<R>> mapper
    );

    Result<T> onSuccess(
            Consumer<? super T> action
    );

    Result<T> onFailure(
            Consumer<? super Throwable> action
    );

    T orElse(
            T other
    );

    T orElseGet(
            Function<? super Throwable, ? extends T> mapper
    );

    Optional<T> toOptional();

    record Success<T>(
            T value
    ) implements Result<T> {

        public Success {
            Objects.requireNonNull(
                    value,
                    "success value must not be null"
            );
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public Throwable cause() {
            throw new NoSuchElementException(
                    "Success has no cause"
            );
        }

        @Override
        public <R> Result<R> map(
                Function<? super T, ? extends R> mapper
        ) {
            Objects.requireNonNull(
                    mapper,
                    "mapper must not be null"
            );

            return Result.of(
                    () -> mapper.apply(value)
            );
        }

        @Override
        public <R> Result<R> flatMap(
                Function<? super T, Result<R>> mapper
        ) {
            Objects.requireNonNull(
                    mapper,
                    "mapper must not be null"
            );

            return Objects.requireNonNull(
                    mapper.apply(value),
                    "flatMap mapper must not return null"
            );
        }

        @Override
        public Result<T> onSuccess(
                Consumer<? super T> action
        ) {
            Objects.requireNonNull(
                    action,
                    "action must not be null"
            ).accept(value);

            return this;
        }

        @Override
        public Result<T> onFailure(
                Consumer<? super Throwable> action
        ) {
            Objects.requireNonNull(
                    action,
                    "action must not be null"
            );

            return this;
        }

        @Override
        public T orElse(
                T other
        ) {
            return value;
        }

        @Override
        public T orElseGet(
                Function<? super Throwable, ? extends T> mapper
        ) {
            Objects.requireNonNull(
                    mapper,
                    "mapper must not be null"
            );

            return value;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.of(value);
        }
    }

    record Failure<T>(
            Throwable cause
    ) implements Result<T> {

        public Failure {
            Objects.requireNonNull(
                    cause,
                    "failure cause must not be null"
            );
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public T value() {
            throw new NoSuchElementException(
                    "Failure has no value",
                    cause
            );
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Result<R> map(
                Function<? super T, ? extends R> mapper
        ) {
            Objects.requireNonNull(
                    mapper,
                    "mapper must not be null"
            );

            return (Result<R>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <R> Result<R> flatMap(
                Function<? super T, Result<R>> mapper
        ) {
            Objects.requireNonNull(
                    mapper,
                    "mapper must not be null"
            );

            return (Result<R>) this;
        }

        @Override
        public Result<T> onSuccess(
                Consumer<? super T> action
        ) {
            Objects.requireNonNull(
                    action,
                    "action must not be null"
            );

            return this;
        }

        @Override
        public Result<T> onFailure(
                Consumer<? super Throwable> action
        ) {
            Objects.requireNonNull(
                    action,
                    "action must not be null"
            ).accept(cause);

            return this;
        }

        @Override
        public T orElse(
                T other
        ) {
            return other;
        }

        @Override
        public T orElseGet(
                Function<? super Throwable, ? extends T> mapper
        ) {
            return Objects.requireNonNull(
                    mapper,
                    "mapper must not be null"
            ).apply(cause);
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }
    }
}
