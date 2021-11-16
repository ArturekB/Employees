package com.example.employees;

import com.example.employees.api.EmployeeService;
import com.example.employees.domain.dto.EmployeeRepresentation;
import com.example.employees.domain.dto.EmployeeSearchParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService service) {
        this.employeeService = service;
    }

    @GetMapping(produces = "application/json")
    public List<EmployeeRepresentation> findAll() {
        return employeeService.findAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public EmployeeRepresentation get(@PathVariable("id") String id) {
        return employeeService.get(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public EmployeeRepresentation create(@RequestBody EmployeeRepresentation employee) {
        return employeeService.create(employee);
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public void update(@PathVariable("id") String id, @RequestBody EmployeeRepresentation employee) {
        employeeService.update(employee.withId(id));
    }

    @DeleteMapping(path = "/{id}")
    public void remove(@PathVariable("id") String id) {
        employeeService.delete(id);
    }

    @GetMapping(path = "/search")
    public List<EmployeeRepresentation> findAll(@RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "surname", required = false) String surname,
                                                @RequestParam(value = "grade", required = false) String grade,
                                                @RequestParam(value = "currency", required = false) String currency,
                                                @RequestParam(value = "amountGreaterThan", required = false) BigDecimal amountGreaterThan,
                                                @RequestParam(value = "amountLessThan", required = false) BigDecimal amountLessThan) {
        return employeeService.findAll(EmployeeSearchParams.builder()
                .name(name)
                .surename(surname)
                .grade(grade)
                .currency(currency)
                .amountGreaterThan(amountGreaterThan)
                .amountLessThan(amountLessThan)
                .build());
    }

}
