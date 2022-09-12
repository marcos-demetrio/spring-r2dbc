package com.example.r2dbc.exception;

import java.util.UUID;

public class ProductNotFoundException extends NotFoundException {
  private static final String MESSAGE = "Product with id %s was not found";

  public ProductNotFoundException(final UUID productId) {
    super(String.format(MESSAGE, productId));
  }
}
