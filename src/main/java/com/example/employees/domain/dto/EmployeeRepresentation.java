package com.example.employees.domain.dto;

import com.example.employees.domain.Employee;
import com.example.employees.domain.Salary;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

@Builder
@With
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class EmployeeRepresentation {

    private String id;
    private String name;
    private String surname;
    private String grade;
    private Salary salary;

    public EmployeeRepresentation(Employee employee) {
        id = employee.getId();
        name = employee.getName();
        surname = employee.getSurname();
        grade = employee.getGrade();
        salary = employee.getSalary();
    }

    public Employee toEntity() {
        return new Employee(id, name, surname, grade, Salary.of(salary));
    }

    public boolean hasId() {
        return Strings.isNotBlank(id);
    }
}
