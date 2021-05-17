package com.itechart.assignment.controller;

import com.itechart.assignment.model.Employee;
import com.itechart.assignment.model.EmployeeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeRequestMapper {

    @Mapping(target = "birthday", dateFormat = "yyyy-MM-dd")
    Employee requestToEmployee(EmployeeRequest employee);
}
