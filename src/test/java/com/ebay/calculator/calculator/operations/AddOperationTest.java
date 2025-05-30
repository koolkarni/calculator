package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddOperationTest {

    private final AddOperation addOperation = new AddOperation();

    @Test
    void getSupportedOperation_shouldReturnAdd() {
        assertEquals(Operation.ADD, addOperation.getSupportedOperation());
    }

    @Test
    void apply_shouldReturnCorrectSum() {
        assertEquals(5.0, addOperation.apply(2.0, 3.0));
        assertEquals(-1.0, addOperation.apply(2.0, -3.0));
        assertEquals(0.0, addOperation.apply(0.0, 0.0));
    }
}
