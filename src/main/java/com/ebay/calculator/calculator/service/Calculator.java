package com.ebay.calculator.calculator.service;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.model.Operation;
import com.ebay.calculator.calculator.operations.OperationStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Calculator {

    private final List<OperationStrategy> strategies;

    private Map<Operation, OperationStrategy> getStrategyMap() {
        return strategies.stream().collect(Collectors.toMap(
                OperationStrategy::getSupportedOperation, s -> s
        ));
    }

    public double calculate(Operation op, double a, double b) {
        OperationStrategy strategy = getStrategyMap().get(op);
        if (strategy == null) {
            throw new UnsupportedOperationException("Unsupported operation: " + op);
        }
        return strategy.apply(a, b);
    }

    public double chain(double initial, List<Operation> ops, List<Double> values) {
        if (ops.size() != values.size()) {
            throw new IllegalArgumentException("Mismatch between operations and values");
        }
        double result = initial;
        for (int i = 0; i < ops.size(); i++) {
            result = calculate(ops.get(i), result, values.get(i));
        }
        return result;
    }

    public List<Double> chainInParallel(List<ChainRequest> requests) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<Double>> tasks = requests.stream()
                .map(req -> (Callable<Double>) () -> chain(req.getInitial(), req.getOperations(), req.getValues()))
                .collect(Collectors.toList());

        try {
            List<Future<Double>> futures = executor.invokeAll(tasks);
            executor.shutdown();
            return futures.stream().map(f -> {
                try {
                    return f.get();
                } catch (Exception e) {
                    log.error("Threaded chain execution failed", e);
                    return Double.NaN;
                }
            }).collect(Collectors.toList());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Parallel execution interrupted", e);
        }
    }
}
