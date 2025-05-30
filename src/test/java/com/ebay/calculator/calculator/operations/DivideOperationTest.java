package com.ebay.calculator.calculator.operations;

import com.ebay.calculator.calculator.model.Operation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DivideOperationTest {

    @Test
    void testGetSupportedOperation() {
        DivideOperation op = new DivideOperation();
        assertEquals(Operation.DIVIDE, op.getSupportedOperation());
    }

    @Test
    void testApply() {
        DivideOperation op = new DivideOperation();
        assertEquals(2.0, op.apply(6, 3));
    }
}