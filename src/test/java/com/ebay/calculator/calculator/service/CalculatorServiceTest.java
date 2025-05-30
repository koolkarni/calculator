package com.ebay.calculator.calculator.service;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.model.Operation;
import com.ebay.calculator.calculator.operations.OperationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculatorServiceTest {

    private Calculator calculator;
    private OperationStrategy addStrategy;
    private OperationStrategy subStrategy;

    @BeforeEach
    void setup() {
        addStrategy = mock(OperationStrategy.class);
        subStrategy = mock(OperationStrategy.class);

        when(addStrategy.getSupportedOperation()).thenReturn(Operation.ADD);
        when(addStrategy.apply(anyDouble(), anyDouble())).thenAnswer(invocation -> {
            double a = invocation.getArgument(0);
            double b = invocation.getArgument(1);
            return a + b;
        });

        when(subStrategy.getSupportedOperation()).thenReturn(Operation.SUBTRACT);
        when(subStrategy.apply(anyDouble(), anyDouble())).thenAnswer(invocation -> {
            double a = invocation.getArgument(0);
            double b = invocation.getArgument(1);
            return a - b;
        });

        calculator = new Calculator(List.of(addStrategy, subStrategy));
    }

    @Test
    void testCalculate_addOperation() {
        double result = calculator.calculate(Operation.ADD, 5, 3);
        assertEquals(8, result);
    }

    @Test
    void testCalculate_unsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () ->
                calculator.calculate(Operation.MULTIPLY, 5, 3));
    }

    @Test
    void testChain_basicOperations() {
        double result = calculator.chain(10, List.of(Operation.ADD, Operation.SUBTRACT), List.of(5.0, 3.0));
        assertEquals(12, result);
    }

    @Test
    void testChain_mismatchedInputs() {
        assertThrows(IllegalArgumentException.class, () ->
                calculator.chain(10, List.of(Operation.ADD), List.of(5.0, 2.0)));
    }

    @Test
    void testChainInParallel_success() {
        ChainRequest req1 = new ChainRequest(10, List.of(Operation.ADD), List.of(5.0));
        ChainRequest req2 = new ChainRequest(20, List.of(Operation.SUBTRACT), List.of(4.0));

        List<Double> results = calculator.chainInParallel(List.of(req1, req2));

        assertEquals(List.of(15.0, 16.0), results);
    }

    @Test
    void testChainInParallel_handlesFailure() {
        ChainRequest req = new ChainRequest(10, List.of(Operation.MULTIPLY), List.of(2.0));
        List<Double> results = calculator.chainInParallel(List.of(req));
        assertEquals(1, results.size());
        assertTrue(Double.isNaN(results.get(0)));
    }
}
