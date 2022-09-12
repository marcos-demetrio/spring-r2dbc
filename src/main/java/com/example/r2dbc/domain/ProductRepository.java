package com.example.r2dbc.domain;

import java.util.UUID;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
  Mono<Long> count();

  Flux<ProductEntity> findAllBy(Pageable pageable);

  Mono<ProductEntity> findById(UUID productId);

  Mono<Boolean> exists(UUID productId);

  Mono<ProductPeriodEffectEntity> findPeriodEffectById(UUID periodEffectId);

  Mono<UUID> findFirstPeriodEffectIdByProductId(UUID productId);

  Mono<Void> deleteProduct(UUID productId);

  Mono<Void> deletePeriodEffectById(UUID periodEffectId);

  Mono<ProductEntity> saveProduct(ProductEntity entity);

  Mono<ProductPeriodEffectEntity> createPeriodEffect(ProductPeriodEffectEntity entity);

  Mono<Void> updateProductPeriodEffect(ProductPeriodEffectEntity entity);
}
