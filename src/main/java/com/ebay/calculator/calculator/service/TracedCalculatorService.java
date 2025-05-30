package com.ebay.calculator.calculator.service;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.model.Operation;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TracedCalculatorService {

    private final Calculator calculator;
    private final Tracer tracer;

    public double tracedCalculate(Operation op, double a, double b) {
        Span span = tracer.spanBuilder("calculator.singleOperation")
                .setAttribute("operation", op.name())
                .setAttribute("input.num1", a)
                .setAttribute("input.num2", b)
                .startSpan();
        try {
            double result = calculator.calculate(op, a, b);
            span.setAttribute("result", result);
            return result;
        } finally {
            span.end();
        }
    }

    public double tracedChain(ChainRequest request) {
        Span span = tracer.spanBuilder("calculator.chain")
                .setAttribute("initial", request.getInitial())
                .setAttribute("ops.count", request.getOperations().size())
                .startSpan();
        try {
            double result = calculator.chain(request.getInitial(), request.getOperations(), request.getValues());
            span.setAttribute("result", result);
            return result;
        } finally {
            span.end();
        }
    }

    public List<Double> tracedChainInParallel(List<ChainRequest> requests) {
        Span span = tracer.spanBuilder("calculator.parallelChain")
                .setAttribute("batchSize", requests.size())
                .startSpan();
        try {
            List<Double> results = calculator.chainInParallel(requests);
            span.setAttribute("success", true);
            return results;
        } catch (Exception e) {
            span.setAttribute("error", true);
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }
}
