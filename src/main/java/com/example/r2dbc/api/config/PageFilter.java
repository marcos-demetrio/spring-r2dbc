package com.example.r2dbc.api.config;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PageFilter {
  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_SIZE = 25;

  @Min(value = 0, message = "Page must be equals or greater than zero")
  private int page;

  @Min(value = 1, message = "Page must be greater than zero")
  @Max(value = 50, message = "Page must be equals or less than 50")
  private int size;

  public PageFilter() {
    this.page = DEFAULT_PAGE;
    this.size = DEFAULT_SIZE;
  }

  public Pageable toPageable() {
    return PageRequest.of(page, size);
  }
}
