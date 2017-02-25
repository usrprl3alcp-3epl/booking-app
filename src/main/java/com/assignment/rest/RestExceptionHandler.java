package com.assignment.rest;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public HttpEntity<ErrorResponse> throwable(final Throwable ex) {
    logger.error("Unknown Exception occurred", ex);
    return new HttpEntity<>(new ErrorResponse<>(ex));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public HttpEntity<ErrorResponse> handleValidationExceptions(
      final ConstraintViolationException ex) {
    Set<String> errors = ex.getConstraintViolations()
        .stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    ErrorResponse<Set<String>> errorResponse = new ErrorResponse<>(errors);

    return new HttpEntity<>(errorResponse);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public HttpEntity<ErrorResponse> handleResourceNotFoundException(
      final ResourceNotFoundException ex) {
    return new HttpEntity<>(ErrorResponse.anErrorMessage(ex.getMessage()));
  }

}