package com.itechart.assignment.service;

import com.itechart.assignment.model.Employee;
import com.itechart.assignment.model.EmployeeState;
import com.itechart.assignment.repositories.EmployeeRepository;
import com.itechart.assignment.service.exceptions.EmployeeNotFoundException;
import com.itechart.assignment.service.exceptions.StateTransitionNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@Validated
public class EmployeeService {

    EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee addEmployee(@Validated Employee employee) {
        employee.setId(null);
        employee.setState(EmployeeState.ADDED);
        return repository.save(employee);
    }

    public void updateState(Integer id, EmployeeState newState) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        if (!employee.getState().isTransitionAllowed(newState)) {
            log.error("Transition from {} to {} is not allowed for employee {}", employee.getState(), newState, id);
            throw new StateTransitionNotAllowedException(newState);
        }
        employee.setState(newState);
        repository.save(employee);
    }

    public Employee getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }
}
