package com.example.employees.domain.dto;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@Builder
public class EmployeeSearchParams {

    private String name;
    private String surename;
    private String grade;
    private BigDecimal amountGreaterThan;
    private BigDecimal amountLessThan;
    private String currency;
}
