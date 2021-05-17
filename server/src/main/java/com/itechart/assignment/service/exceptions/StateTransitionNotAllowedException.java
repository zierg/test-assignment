package com.itechart.assignment.service.exceptions;

import com.itechart.assignment.model.EmployeeState;

public class StateTransitionNotAllowedException extends IllegalArgumentException {

    public StateTransitionNotAllowedException(EmployeeState toState) {
        super("Transition to state " + toState + " is not allowed");
    }
}
