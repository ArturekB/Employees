package com.example.employees.api.implementation;

import com.example.employees.domain.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class EmployeeSpecs {

    private static final Specification<Employee> ACCEPT_ALL = ((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

    public static Specification<Employee> nameEquals(String value) {
        return stringEqualsIfPresent("name", value);
    }

    public static Specification<Employee> surnameEquals(String value) {
        return stringEqualsIfPresent("surname", value);
    }

    public static Specification<Employee> gradeEquals(String value) {
        return stringEqualsIfPresent("grade", value);
    }

    public static Specification<Employee> currencyEquals(String value) {
        return !StringUtils.hasText(value) ? ACCEPT_ALL : ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("salary").get("currency"), value));
    }

    public static Specification<Employee> amountGreaterThan(BigDecimal value) {
        return value != null ? ACCEPT_ALL : ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("salary").get("amount"), value));
    }

    public static Specification<Employee> amountLessThan(BigDecimal value) {
        return value != null ? ACCEPT_ALL : ((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("salary").get("amount"), value));
    }

    public static Specification<Employee> stringEqualsIfPresent(String param, String value) {
        return !StringUtils.hasText(value) ? ACCEPT_ALL : ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(param), value));
    }

}
