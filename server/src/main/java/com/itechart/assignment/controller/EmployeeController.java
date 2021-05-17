package com.itechart.assignment.controller;

import com.itechart.assignment.model.Employee;
import com.itechart.assignment.model.EmployeeRequest;
import com.itechart.assignment.model.EmployeeState;
import com.itechart.assignment.service.EmployeeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/employees")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Validated
public class EmployeeController {

    EmployeeService service;
    EmployeeRequestMapper mapper;

    public EmployeeController(EmployeeService service,
                              EmployeeRequestMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public Employee addEmployee(@RequestBody @Validated EmployeeRequest request) {
        var employee = mapper.requestToEmployee(request);
        return service.addEmployee(employee);
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable("id") Integer id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/state")
    public void updateState(@PathVariable("id") Integer id, @NotNull @Validated @RequestBody EmployeeState newState) {
        log.info("Update state request. Employee id = {}, target state = {}", id, newState);
        service.updateState(id, newState);
    }
}
