package com.example.r2dbc.api.v1;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPeriodEffectResponse {
  private UUID id;
  private LocalDateTime periodEffect;
  private String name;
  private BigDecimal price;
  private BigDecimal discount;
}
