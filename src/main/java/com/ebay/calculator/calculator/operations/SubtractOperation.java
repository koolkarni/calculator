package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.springframework.stereotype.Component;

@Component
public class SubtractOperation implements OperationStrategy {

    @Override
    public Operation getSupportedOperation() {
        return Operation.SUBTRACT;
    }

    @Override
    public double apply(double a, double b) {
        return a - b;
    }
}
