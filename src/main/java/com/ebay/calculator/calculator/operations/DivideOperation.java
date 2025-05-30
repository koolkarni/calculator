package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class DivideOperation implements OperationStrategy {

    @Override
    public Operation getSupportedOperation() {
        return Operation.DIVIDE;
    }

    @Override
    public double apply(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return a / b;
    }
}
