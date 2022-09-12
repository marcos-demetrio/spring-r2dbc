package com.example.r2dbc.exception;

import java.util.UUID;

public class ProductPeriodEffectNotFoundException extends NotFoundException {
  private static final String MESSAGE = "Product Period Effect with id %s was not found";

  public ProductPeriodEffectNotFoundException(final UUID productId) {
    super(String.format(MESSAGE, productId));
  }
}
