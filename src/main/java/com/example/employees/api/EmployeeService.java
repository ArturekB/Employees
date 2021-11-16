package com.example.employees.api;

import com.example.employees.domain.dto.EmployeeRepresentation;
import com.example.employees.domain.dto.EmployeeSearchParams;

import java.util.List;

public interface EmployeeService {

    void delete(String id);

    EmployeeRepresentation create(EmployeeRepresentation employee);

    void update(EmployeeRepresentation employee);

    EmployeeRepresentation get(String id);

    List<EmployeeRepresentation> findAll();

    List<EmployeeRepresentation> findAll(EmployeeSearchParams params);
}
