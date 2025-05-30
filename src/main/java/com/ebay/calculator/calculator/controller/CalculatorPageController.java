package com.ebay.calculator.calculator.controller;

import com.ebay.calculator.calculator.dto.ChainRequest;
import com.ebay.calculator.calculator.dto.SingleCalcRequest;
import com.ebay.calculator.calculator.service.Calculator;
import com.ebay.calculator.calculator.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping({"/ui", "/ui/"})
@RequiredArgsConstructor
public class CalculatorPageController {

    private final Calculator calculator;
    private final HistoryService historyService;

    @GetMapping
    public String showForm(Model model) {
        log.debug("Displaying calculator form");
        if (!model.containsAttribute("singleRequest")) {
            model.addAttribute("singleRequest", new SingleCalcRequest());
        }
        if (!model.containsAttribute("chainRequest")) {
            model.addAttribute("chainRequest", new ChainRequest());
        }
        // Load history if not already loaded
        if (!model.containsAttribute("history")) {
            try {
                var history = historyService.getRecent(10);
                model.addAttribute("history", history);
            } catch (Exception e) {
                log.error("Failed to retrieve history. Error: {}", e.getMessage(), e);
                model.addAttribute("error", "Failed to load history");
            }
        }
        return "calculator";
    }

    @PostMapping("/single")
    public String calculateSingle(@ModelAttribute("singleRequest") SingleCalcRequest request, Model model) {
        try {
            double result = calculator.calculate(request.getOperation(), request.getNum1(), request.getNum2());
            log.info("Calculation successful: {} {} {} = {}",
                    request.getNum1(), request.getOperation(), request.getNum2(), result);
            model.addAttribute("calculationResult", result);
            model.addAttribute("error", null);
        } catch (Exception e) {
            log.error("Calculation failed", e);
            model.addAttribute("error", e.getMessage());
        }
        return showForm(model);
    }

    @PostMapping("/chain")
    public String calculateChain(@ModelAttribute("chainRequest") ChainRequest request, Model model) {
        log.info("Received chain calculation request. Initial: {}, Operations count: {}",
                request.getInitial(), request.getOperations().size());

        try {
            double result = calculator.chain(
                    request.getInitial(),
                    request.getOperations(),
                    request.getValues()
            );
            log.info("Chain calculation successful. Result: {}", result);
            model.addAttribute("chainResult", result);
            model.addAttribute("error", null);
        } catch (Exception e) {
            log.error("Chain calculation failed for request: {}. Error: {}", request, e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
        }
        return showForm(model);
    }

    @GetMapping("/history")
    public String showHistory(Model model) {
        log.debug("Fetching calculation history");
        try {
            var history = historyService.getRecent(10);
            log.info("Retrieved {} history entries", history.size());
            model.addAttribute("history", history);
        } catch (Exception e) {
            log.error("Failed to retrieve history. Error: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load history");
        }
        return showForm(model);
    }
}