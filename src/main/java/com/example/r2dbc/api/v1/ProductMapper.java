package com.example.r2dbc.api.v1;

import com.example.r2dbc.domain.ProductEntity;
import com.example.r2dbc.domain.ProductPeriodEffectDto;
import com.example.r2dbc.domain.ProductPeriodEffectEntity;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public class ProductMapper {
  public ProductResponse toResponse(final ProductEntity entity) {
    return ProductResponse.builder()
        .id(entity.getId())
        .registrationDate(entity.getRegistrationDate())
        .periodEffects(
            entity.getPeriodEffects().stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toSet()))
        .build();
  }

  public ProductPeriodEffectResponse toResponse(final ProductPeriodEffectEntity entity) {
    return ProductPeriodEffectResponse.builder()
        .id(entity.getId())
        .periodEffect(entity.getPeriodEffect())
        .name(entity.getName())
        .price(entity.getPrice())
        .discount(entity.getDiscount())
        .build();
  }

  public Page<ProductResponse> toResponse(final Page<ProductEntity> entities) {
    return entities.map(ProductMapper::toResponse);
  }

  public ProductPeriodEffectDto toDto(final ProductPeriodEffectUpdateRequest request) {
    return ProductPeriodEffectDto.builder()
        .name(request.getName())
        .price(request.getPrice())
        .discount(request.getDiscount())
        .build();
  }

  public ProductEntity toEntity(final ProductPeriodEffectCreateRequest request) {
    var periodEffectEntity = toProductPeriodEffectEntity(request);

    return ProductEntity.builder()
        .registrationDate(periodEffectEntity.getPeriodEffect())
        .periodEffects(Set.of(periodEffectEntity))
        .build();
  }

  public ProductPeriodEffectEntity toProductPeriodEffectEntity(
      final ProductPeriodEffectCreateRequest request) {
    return ProductPeriodEffectEntity.builder()
        .periodEffect(request.getPeriodEffect())
        .name(request.getName())
        .price(request.getPrice())
        .discount(request.getDiscount())
        .build();
  }

  public ProductPeriodEffectEntity toProductPeriodEffectEntity(
      final UUID productId, final ProductPeriodEffectCreateRequest request) {
    var entity = toProductPeriodEffectEntity(request);

    entity.setProductId(productId);

    return entity;
  }
}
