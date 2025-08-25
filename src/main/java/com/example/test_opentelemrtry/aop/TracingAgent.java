package com.example.test_opentelemrtry.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

@Aspect
@Component
public class TracingAgent {
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.auto");

    @Around("@annotation(com.example.test_opentelemrtry.aop.Traceable)")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable{
        String spanName = joinPoint.getSignature().toShortString();

        Span span =  tracer.spanBuilder(spanName)
                        .setParent(Context.current())
                        .startSpan();
        

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
