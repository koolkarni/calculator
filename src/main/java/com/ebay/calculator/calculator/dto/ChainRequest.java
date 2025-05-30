package com.ebay.calculator.calculator.dto;

import com.ebay.calculator.calculator.model.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChainRequest {
    private double initial;
    private List<Operation> operations;
    private List<Double> values;
}
