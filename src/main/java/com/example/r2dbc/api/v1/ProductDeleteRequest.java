package com.example.r2dbc.api.v1;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDeleteRequest {
  @Size(min = 1, max = 50, message = "Ids must be between 1 and 50")
  @NotNull(message = "Ids are required")
  private List<UUID> ids;
}
