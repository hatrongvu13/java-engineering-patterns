package com.htv.patterns.behavioral.observer.publisher;

import com.htv.patterns.behavioral.observer.core.DomainEvent;
import com.htv.patterns.behavioral.observer.core.DomainEventListener;
import com.htv.patterns.behavioral.observer.core.DomainEventPublisher;
import com.htv.patterns.behavioral.observer.core.EventListenerSubscription;
import com.htv.patterns.behavioral.observer.core.EventPublicationException;
import com.htv.patterns.behavioral.observer.core.EventPublicationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class SimpleDomainEventPublisher
        implements DomainEventPublisher {

    private final List<
            DomainEventListener<? extends DomainEvent>
            > listeners =
            new CopyOnWriteArrayList<>();

    private final boolean failFast;

    public SimpleDomainEventPublisher() {
        this(false);
    }

    public SimpleDomainEventPublisher(
            boolean failFast
    ) {
        this.failFast = failFast;
    }

    @Override
    public EventListenerSubscription subscribe(
            DomainEventListener<? extends DomainEvent> listener
    ) {
        Objects.requireNonNull(
                listener,
                "listener must not be null"
        );

        if (listeners.contains(listener)) {
            throw new IllegalArgumentException(
                    "Listener has already been registered: "
                            + listener.listenerName()
            );
        }

        listeners.add(listener);

        return () -> listeners.remove(listener);
    }

    @Override
    public EventPublicationResult publish(
            DomainEvent event
    ) {
        Objects.requireNonNull(
                event,
                "event must not be null"
        );

        List<
                DomainEventListener<? extends DomainEvent>
                > matchingListeners =
                findMatchingListeners(event);

        List<
                EventPublicationResult.ListenerFailure
                > failures = new ArrayList<>();

        int successCount = 0;

        for (
                DomainEventListener<? extends DomainEvent> listener
                : matchingListeners
        ) {
            try {
                invoke(listener, event);
                successCount++;
            } catch (RuntimeException exception) {
                if (failFast) {
                    throw new EventPublicationException(
                            event.eventType(),
                            listener.listenerName(),
                            exception
                    );
                }

                failures.add(
                        new EventPublicationResult.ListenerFailure(
                                listener.listenerName(),
                                failureMessage(exception),
                                exception
                        )
                );
            }
        }

        return new EventPublicationResult(
                event.eventType(),
                matchingListeners.size(),
                successCount,
                failures
        );
    }

    @Override
    public int listenerCount() {
        return listeners.size();
    }

    private List<
            DomainEventListener<? extends DomainEvent>
            > findMatchingListeners(
            DomainEvent event
    ) {
        return listeners.stream()
                .filter(
                        listener ->
                                listener.eventType()
                                        .isAssignableFrom(
                                                event.getClass()
                                        )
                )
                .toList();
    }

    @SuppressWarnings("unchecked")
    private static <E extends DomainEvent>
    void invoke(
            DomainEventListener<? extends DomainEvent> listener,
            DomainEvent event
    ) {
        DomainEventListener<E> typedListener =
                (DomainEventListener<E>) listener;

        typedListener.onEvent((E) event);
    }

    private static String failureMessage(
            RuntimeException exception
    ) {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            return exception
                    .getClass()
                    .getSimpleName();
        }

        return message;
    }
}