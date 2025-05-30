package com.ebay.calculator.calculator.dto;

import com.ebay.calculator.calculator.model.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleCalcRequest {

    private Operation operation;
    private double num1;
    private double num2;
}