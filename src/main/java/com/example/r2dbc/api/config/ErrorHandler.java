package com.example.r2dbc.api.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.r2dbc.exception.DomainException;
import com.example.r2dbc.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorResponse> handleDomainException(DomainException e) {
    var errorResponse = ErrorResponse.create(e.getMessage());

    return ResponseEntity.status(BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    var errorResponse = ErrorResponse.create(e.getMessage());

    return ResponseEntity.status(NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<List<ErrorResponse>> handleWebExchangeBindException(
      WebExchangeBindException e) {
    var errors =
        e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .map(ErrorResponse::create)
            .collect(Collectors.toList());

    return ResponseEntity.badRequest().body(errors);
  }
}
