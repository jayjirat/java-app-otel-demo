package com.example.test_opentelemrtry.utils;

import org.springframework.stereotype.Component;

import com.example.test_opentelemrtry.aop.Traceable;

@Component
public class UtilB {

    // private final Tracer tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.auto");

    @Traceable
    public String compute() {
    //     Span span = tracer.spanBuilder("UtilB.compute")
    // .setParent(Context.current())
    // .startSpan();

        try {
            Thread.sleep(1000);
            // span.setAttribute("utilB.sleepDuration", 1000);
            return "DataB";
        } catch (InterruptedException e) {
            // span.recordException(e);
            throw new RuntimeException(e);
        } finally {
            // span.end();
        }
    }
}

