package com.example.employees.api.implementation;

import com.example.employees.api.EmployeeService;
import com.example.employees.domain.EmployeeRepository;
import com.example.employees.domain.dto.EmployeeRepresentation;
import com.example.employees.domain.dto.EmployeeSearchParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.employees.api.implementation.EmployeeSpecs.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final String ALREADY_EXISTS_MESSAGE_TEMPLATE = "Employee with id: %s already exists";
    private static final String NOT_FOUND_MESSAGE_TEMPLATE = "Employee with id: %s not found";
    private final EmployeeRepository repository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throwIllegalStateException(NOT_FOUND_MESSAGE_TEMPLATE, id);
        }
        repository.deleteById(id);
    }

    @Override
    public EmployeeRepresentation create(EmployeeRepresentation representation) {
        if (representation.hasId() && repository.existsById(representation.getId())) {
            throwIllegalStateException(ALREADY_EXISTS_MESSAGE_TEMPLATE, representation.getId());
        }
        return new EmployeeRepresentation(repository.save(representation.withId(null).toEntity()));
    }

    private void throwIllegalStateException(String message, String id) {
        throw new IllegalStateException(String.format(message, id));
    }

    @Override
    public void update(EmployeeRepresentation representation) {
        final String id = representation.getId();
        if (!repository.existsById(id)) {
            throwIllegalStateException(NOT_FOUND_MESSAGE_TEMPLATE, id);
        }
        repository.save(representation.toEntity());
    }

    @Override
    public EmployeeRepresentation get(String id) {
        return new EmployeeRepresentation(repository.getById(id));
    }

    @Override
    public List<EmployeeRepresentation> findAll() {
        return repository.findAll()
                .stream()
                .map(EmployeeRepresentation::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeRepresentation> findAll(EmployeeSearchParams params) {
        return repository.findAll(where(nameEquals(params.getName())
                .and(surnameEquals(params.getSurename()))
                .and(gradeEquals(params.getGrade()))
                .and(currencyEquals(params.getCurrency()))
                .and(amountLessThan(params.getAmountLessThan()))
                .and(amountGreaterThan(params.getAmountGreaterThan()))))
                .stream()
                .map(EmployeeRepresentation::new)
                .collect(Collectors.toList());
    }
}
