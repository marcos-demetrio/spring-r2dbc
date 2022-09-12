package com.example.r2dbc.domain;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductPeriodEffectDto {
  private String name;
  private BigDecimal price;
  private BigDecimal discount;
}
