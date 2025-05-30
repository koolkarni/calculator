package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;

public interface OperationStrategy {
    Operation getSupportedOperation();
    double apply(double a, double b);
}
