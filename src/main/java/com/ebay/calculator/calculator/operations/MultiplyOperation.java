package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class MultiplyOperation implements OperationStrategy {

    @Override
    public Operation getSupportedOperation() {
        return Operation.MULTIPLY;
    }

    @Override
    public double apply(double a, double b) {
        return a * b;
    }
}
