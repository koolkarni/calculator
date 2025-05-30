package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MultiplyOperationTest {

    @Test
    void testGetSupportedOperation() {
        MultiplyOperation op = new MultiplyOperation();
        assertEquals(Operation.MULTIPLY, op.getSupportedOperation());
    }

    @Test
    void testApply() {
        MultiplyOperation op = new MultiplyOperation();
        assertEquals(6.0, op.apply(2, 3));
    }
}