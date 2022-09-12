package com.example.r2dbc.api.v1;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "create")
public class ProductPeriodEffectCreateRequest {
  @NotNull(message = "Period Effect is required")
  private LocalDateTime periodEffect;

  @NotNull(message = "Name is required")
  private String name;

  @Min(value = 0, message = "Price must be equal to or greater than zero")
  @NotNull(message = "Price is required")
  private BigDecimal price;

  @Max(value = 100, message = "Discount must be less than 100")
  @Min(value = 0, message = "Discount must be equal to or greater than zero")
  @NotNull(message = "Discount is required")
  private BigDecimal discount;
}
