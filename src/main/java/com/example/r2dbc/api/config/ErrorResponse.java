package com.example.r2dbc.api.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "create")
public class ErrorResponse {
  private String message;
}
