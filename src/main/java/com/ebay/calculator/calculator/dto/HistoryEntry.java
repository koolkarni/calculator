package com.ebay.calculator.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntry {
    private Object input;  // Can be SingleCalcRequest or ChainRequest
    private double result;
    private String timestamp;
}