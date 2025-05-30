package com.ebay.calculator.calculator.controller;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.dto.SingleCalcRequest;
import com.ebay.calculator.calculator.dto.ThreadedChainRequest;
import com.ebay.calculator.calculator.service.Calculator;
import com.ebay.calculator.calculator.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public double calculate(@RequestBody SingleCalcRequest request) {
        log.info("Single op: {}", request);
        return calculator.calculate(request.getOperation(), request.getNum1(), request.getNum2());
    }

    @PostMapping("/chain")
    public double chain(@RequestBody ChainRequest request) {
        log.info("Sequential chain request: {}", request);
        double result = calculator.chain(request.getInitial(), request.getOperations(), request.getValues());
        historyService.saveHistory(request, result);
        return result;
    }

    @PostMapping("/chain/threaded")
    public List<Double> threadedChain(@RequestBody ThreadedChainRequest request) {
        log.info("Parallel chain request: {} chains", request.getRequests().size());
        return calculator.chainInParallel(request.getRequests());
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        model.addAttribute("history", historyService.getRecent(10));
        model.addAttribute("request", new SingleCalcRequest());
        model.addAttribute("result", null); // Clear any previous result
        return "calculator";
    }
}
