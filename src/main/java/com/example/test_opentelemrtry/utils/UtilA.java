package com.example.test_opentelemrtry.utils;

import org.springframework.stereotype.Component;

import com.example.test_opentelemrtry.aop.Traceable;

@Component
public class UtilA {

    // private final Tracer tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.auto");

    @Traceable
    public String compute() {
    //     Span span = tracer.spanBuilder("UtilA.compute")
    // .setParent(Context.current())
    // .startSpan();

        try {
            Thread.sleep(500);
            // span.setAttribute("utilA.sleepDuration", 500);
            return "DataA";
        } catch (InterruptedException e) {
            // span.recordException(e);
            throw new RuntimeException(e);
        } finally {
            // span.end();
        }
    }
}

