package com.ebay.calculator.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEntry {
    public enum EntryType { SINGLE, CHAIN }

    private Object input;  // Can be SingleCalcRequest or ChainRequest
    private double result;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Instant timestamp;

    private EntryType type;

    // Helper constructor that automatically sets type and timestamp
    public HistoryEntry(Object input, double result) {
        this.input = input;
        this.result = result;
        this.timestamp = Instant.now();
        this.type = input instanceof SingleCalcRequest ? EntryType.SINGLE : EntryType.CHAIN;
    }
}