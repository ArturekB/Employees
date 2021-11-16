package com.example.employees.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Salary {

    @EqualsAndHashCode.Exclude
    private BigDecimal amount;

    private String currency;

    public static Salary of(Salary salary) {
        return salary != null ? new Salary(salary.amount, salary.currency) : null;
    }

    @EqualsAndHashCode.Include
    private BigDecimal getAmountForEquals() {
        return Optional.ofNullable(amount)
                .map(BigDecimal::stripTrailingZeros)
                .orElse(null);
    }
}
