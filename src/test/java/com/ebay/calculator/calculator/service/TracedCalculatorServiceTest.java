package com.ebay.calculator.calculator.service;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.model.Operation;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TracedCalculatorServiceTest {

    @Mock private Calculator calculator;
    @Mock private Tracer tracer;
    @Mock private Span span;
    @Mock private SpanBuilder spanBuilder;

    @InjectMocks private TracedCalculatorService tracedCalculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tracer.spanBuilder(anyString())).thenReturn(spanBuilder);
        when(spanBuilder.setAttribute(anyString(), anyString())).thenReturn(spanBuilder);
        when(spanBuilder.setAttribute(anyString(), anyDouble())).thenReturn(spanBuilder);
        when(spanBuilder.setAttribute(anyString(), anyBoolean())).thenReturn(spanBuilder);
        when(spanBuilder.setAttribute(anyString(), anyLong())).thenReturn(spanBuilder);
        when(spanBuilder.startSpan()).thenReturn(span);
    }

    @Test
    void tracedCalculate_success() {
        when(calculator.calculate(Operation.ADD, 5.0, 3.0)).thenReturn(8.0);
        double result = tracedCalculatorService.tracedCalculate(Operation.ADD, 5.0, 3.0);
        assertEquals(8.0, result);
        verify(span).end();
    }

    @Test
    void tracedCalculate_handlesException() {
        when(calculator.calculate(Operation.DIVIDE, 4.0, 0.0)).thenThrow(new ArithmeticException("Divide by zero"));
        assertThrows(ArithmeticException.class, () -> tracedCalculatorService.tracedCalculate(Operation.DIVIDE, 4.0, 0.0));
        verify(span).end();
    }

    @Test
    void tracedChain_success() {
        ChainRequest req = new ChainRequest(10.0, List.of(Operation.ADD), List.of(5.0));
        when(calculator.chain(anyDouble(), any(), any())).thenReturn(15.0);
        double result = tracedCalculatorService.tracedChain(req);
        assertEquals(15.0, result);
        verify(span).end();
    }

    @Test
    void tracedChain_throwsException() {
        ChainRequest req = new ChainRequest(10.0, List.of(Operation.ADD), List.of(5.0));
        when(calculator.chain(anyDouble(), any(), any())).thenThrow(new RuntimeException("Error"));
        assertThrows(RuntimeException.class, () -> tracedCalculatorService.tracedChain(req));
        verify(span).end();
    }

    @Test
    void tracedChainInParallel_success() {
        ChainRequest r = new ChainRequest(1.0, List.of(Operation.ADD), List.of(1.0));
        when(calculator.chainInParallel(List.of(r))).thenReturn(List.of(2.0));
        List<Double> result = tracedCalculatorService.tracedChainInParallel(List.of(r));
        assertEquals(1, result.size());
        verify(span).end();
    }

    @Test
    void tracedChainInParallel_exceptionHandled() {
        ChainRequest r = new ChainRequest(1.0, List.of(Operation.ADD), List.of(1.0));
        when(calculator.chainInParallel(List.of(r))).thenThrow(new RuntimeException("fail"));
        assertThrows(RuntimeException.class, () -> tracedCalculatorService.tracedChainInParallel(List.of(r)));
        verify(span).recordException(any());
        verify(span).end();
    }
}
