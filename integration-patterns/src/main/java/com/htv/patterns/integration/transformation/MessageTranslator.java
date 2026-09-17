package com.htv.patterns.integration.transformation;

import com.htv.patterns.integration.core.Message;

import java.util.function.Function;

public final class MessageTranslator<T, R> {
    private final Function<T, R> translator;

    public MessageTranslator(Function<T, R> translator) {
        this.translator = translator;
    }

    public Message<R> translate(Message<T> message) {
        return message.mapPayload(translator.apply(message.payload()));
    }
}
