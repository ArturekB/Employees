package com.example.employees.api;

import com.example.employees.api.implementation.EmployeeServiceImpl;
import com.example.employees.domain.Employee;
import com.example.employees.domain.EmployeeRepository;
import com.example.employees.domain.Salary;
import com.example.employees.domain.dto.EmployeeRepresentation;
import com.example.employees.domain.dto.EmployeeSearchParams;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeServiceTest {

    private static final String TEST_NAME = "testName";
    private static final String TEST_SURNAME = "testSurname";
    private static final String TEST_GRADE = "testGrade";

    @Autowired
    private EmployeeRepository repository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeServiceImpl(repository);
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateEmployee() {
        var employeeRepresentation = employeeService.create(prepareTestEmployee());

        Assertions.assertThat(employeeRepresentation).isNotNull();
    }

    @Test
    void shouldThrowWhenCreateEmployeeWithExistingId() {
        var existingEmployeeId = employeeService.create(prepareTestEmployee()).getId();
        var employeeRepresentation = prepareTestEmployee();
        employeeRepresentation.setId(existingEmployeeId);

        Assertions.assertThatThrownBy(()->employeeService.create(employeeRepresentation))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("Employee with id: %s already exists", existingEmployeeId));
    }

    @ParameterizedTest
    @MethodSource
    void shouldSearchEmployees(EmployeeSearchParams params, int numberOfMatches) {
        prepareSearchTestRepositoryContent();
    }

    static Stream<Arguments> shouldSearchEmployees() {
        return Stream.of(
                Arguments.of(EmployeeSearchParams.builder().amountGreaterThan(BigDecimal.valueOf(2)).build(), 1),
                Arguments.of(EmployeeSearchParams.builder().name(TEST_NAME).build(),3));
    }


    private void prepareSearchTestRepositoryContent() {
        var employeeRepresentation = prepareTestEmployee();
        repository.save(employeeRepresentation.toEntity());
        employeeRepresentation.withSalary(new Salary(BigDecimal.TEN,"PLN"));
        repository.save(employeeRepresentation.toEntity());
        employeeRepresentation.withSalary(new Salary(BigDecimal.ONE, "USD"));
        repository.save(employeeRepresentation.toEntity());
        employeeRepresentation.withName("JAN");
        repository.save(employeeRepresentation.toEntity());
    }


    private EmployeeRepresentation prepareTestEmployee() {
        return EmployeeRepresentation.builder()
                .name(TEST_NAME)
                .surname(TEST_SURNAME)
                .grade(TEST_GRADE)
                .build();
    }


}