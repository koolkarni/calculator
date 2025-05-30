package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class AddOperation implements OperationStrategy {

    @Override
    public Operation getSupportedOperation() {
        return Operation.ADD;
    }

    @Override
    public double apply(double a, double b) {
        return a + b;
    }
}
