package com.itechart.assignment.model;

public enum EmployeeState {
    ADDED,
    IN_CHECK,
    APPROVED,
    ACTIVE;

    public boolean isTransitionAllowed(EmployeeState toState) {
        return ADDED != toState || this == toState;
    }
}
