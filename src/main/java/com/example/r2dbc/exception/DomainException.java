package com.example.r2dbc.exception;

public class DomainException extends RuntimeException {
  public DomainException(String message) {
    super(message);
  }
}
