package com.ebay.calculator.calculator.controller;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.dto.SingleCalcRequest;
import com.ebay.calculator.calculator.dto.ThreadedChainRequest;
import com.ebay.calculator.calculator.service.Calculator;
import com.ebay.calculator.calculator.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class CalculatorRestController {

    private final Calculator calculator;
    private final HistoryService historyService;

    @PostMapping("/calculate")
    public ResponseEntity<Double> calculate(@RequestBody SingleCalcRequest request) {
        if (request.getOperation() == null) {
            throw new IllegalArgumentException("Operation must not be null");
        }
        double result = calculator.calculate(request.getOperation(), request.getNum1(), request.getNum2());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/chain")
    public double chain(@RequestBody ChainRequest request) {
        log.info("Sequential chain request: {}", request);
        double result = calculator.chain(request.getInitial(), request.getOperations(), request.getValues());
        try {
            // Store the raw object, not JSON string
            historyService.saveHistory("calculator:history", request, "chain", result);
        } catch (Exception e) {
            log.error("Failed to save history", e);
        }
        return result;
    }

    @PostMapping("/chain/threaded")
    public ResponseEntity<List<Double>> threadedChain(@RequestBody ThreadedChainRequest request) {
        if (request.getRequests() == null || request.getRequests().isEmpty()) {
            throw new IllegalArgumentException("Requests list must not be empty");
        }

        log.info("Parallel chain request: {} chains", request.getRequests().size());
        List<Double> results = calculator.chainInParallel(request.getRequests());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        model.addAttribute("history", historyService.getRecent(10));
        model.addAttribute("request", new SingleCalcRequest());
        model.addAttribute("result", null); // Clear any previous result
        return "calculator";
    }
}
