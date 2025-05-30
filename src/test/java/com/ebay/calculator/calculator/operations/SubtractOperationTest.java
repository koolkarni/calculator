package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtractOperationTest {

    @Test
    void testGetSupportedOperation() {
        SubtractOperation op = new SubtractOperation();
        assertEquals(Operation.SUBTRACT, op.getSupportedOperation());
    }

    @Test
    void testApply() {
        SubtractOperation op = new SubtractOperation();
        assertEquals(1.0, op.apply(3, 2));
    }
}