package com.example.r2dbc.domain;

import static java.util.Objects.isNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("product_period_effect")
public class ProductPeriodEffectEntity implements Persistable<UUID> {

  @Id
  @Column("product_period_effect_id")
  private UUID id;

  @Column("period_effect")
  private LocalDateTime periodEffect;

  @Column("product_id")
  private UUID productId;

  @Column("name")
  private String name;

  @Column("price")
  private BigDecimal price;

  @Column("discount")
  private BigDecimal discount;

  @Override
  public boolean isNew() {
    return isNull(this.id);
  }
}
