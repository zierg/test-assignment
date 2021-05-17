package com.itechart.assignment.controller.exceptions;

import com.itechart.assignment.service.exceptions.StateTransitionNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StateTransitionNotAllowedAdvice {

    @ResponseBody
    @ExceptionHandler(StateTransitionNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String transitionNotAllowedHandler(StateTransitionNotAllowedException ex) {
        return ex.getMessage();
    }
}
