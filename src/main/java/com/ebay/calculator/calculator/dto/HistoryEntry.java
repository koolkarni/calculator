package com.ebay.calculator.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntry {
    private Object input;         // hold actual DTO (SingleCalcRequest or ChainRequest)
    private String inputType;     // "single" or "chain"
    private double result;
    private String timestamp;
}