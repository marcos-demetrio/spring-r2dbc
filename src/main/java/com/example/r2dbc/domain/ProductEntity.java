package com.example.r2dbc.domain;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("product")
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity implements Persistable<UUID> {

  @Id
  @Column("product_id")
  private UUID id;

  @Column("registration_date")
  private LocalDateTime registrationDate;

  @Transient private Set<ProductPeriodEffectEntity> periodEffects;

  @Override
  public boolean isNew() {
    return isNull(this.id);
  }
}
