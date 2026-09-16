package com.htv.patterns.structural.decorator.decorators;

import com.htv.patterns.core.result.Result;
import com.htv.patterns.structural.adapter.core.ChargeReceipt;
import com.htv.patterns.structural.adapter.core.ChargeRequest;
import com.htv.patterns.structural.adapter.core.PaymentGateway;
import com.htv.patterns.structural.decorator.core.PaymentGatewayDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Decorator that records a log line for each charge outcome,
 * without altering the result. Demonstrates transparent
 * behaviour augmentation.
 */
public final class LoggingGatewayDecorator
        extends PaymentGatewayDecorator {

    private final Consumer<String> sink;
    private final List<String> lines = new ArrayList<>();

    public LoggingGatewayDecorator(
            PaymentGateway delegate
    ) {
        this(delegate, null);
    }

    public LoggingGatewayDecorator(
            PaymentGateway delegate,
            Consumer<String> sink
    ) {
        super(delegate);
        this.sink = sink;
    }

    @Override
    public Result<ChargeReceipt> charge(
            ChargeRequest request
    ) {
        Result<ChargeReceipt> result =
                super.charge(request);

        String line = result.isSuccess()
                ? "OK " + provider() + " " + request.reference()
                : "ERR " + provider() + " " + request.reference()
                        + " : " + result.cause().getMessage();

        lines.add(line);

        if (sink != null) {
            sink.accept(line);
        }

        return result;
    }

    public List<String> logLines() {
        return List.copyOf(lines);
    }
}
