package com.example.r2dbc.api.v1;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
  private UUID id;
  private LocalDateTime registrationDate;
  private Set<ProductPeriodEffectResponse> periodEffects;
}
