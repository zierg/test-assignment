package com.itechart.assignment.service.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Integer id) {
        super("Employee with id " + id + " was not found");
    }
}
