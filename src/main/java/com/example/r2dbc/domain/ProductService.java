package com.example.r2dbc.domain;

import static org.springframework.util.ObjectUtils.nullSafeEquals;

import com.example.r2dbc.exception.DomainException;
import com.example.r2dbc.exception.ProductNotFoundException;
import com.example.r2dbc.exception.ProductPeriodEffectNotFoundException;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
public class ProductService {

  @Autowired private ProductRepository repository;

  public Mono<Page<ProductEntity>> findAll(Pageable pageable) {
    return this.repository
        .findAllBy(pageable)
        .collectList()
        .zipWith(this.repository.count())
        .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
  }

  public Mono<ProductEntity> findById(final UUID productId) {
    return this.repository
        .findById(productId)
        .switchIfEmpty(Mono.error(() -> new ProductNotFoundException(productId)));
  }

  public Mono<ProductPeriodEffectEntity> findPeriodEffectById(final UUID periodEffectId) {
    return this.repository
        .findPeriodEffectById(periodEffectId)
        .switchIfEmpty(Mono.error(() -> new ProductPeriodEffectNotFoundException(periodEffectId)));
  }

  @Transactional
  public Mono<ProductEntity> create(final ProductEntity entity) {
    return this.repository.saveProduct(entity);
  }

  @Transactional
  public Mono<Void> deletePeriodEffect(final UUID periodEffectId) {
    return this.findPeriodEffectById(periodEffectId)
        .flatMap(
            entity ->
                this.repository
                    .findFirstPeriodEffectIdByProductId(entity.getProductId())
                    .map(firstPeriodEffectId -> Tuples.of(firstPeriodEffectId, entity)))
        .flatMap(
            objects -> {
              var firstPeriodEffectId = objects.getT1();
              var periodEffectEntity = objects.getT2();

              if (nullSafeEquals(firstPeriodEffectId, periodEffectId)) {
                return Mono.error(new DomainException("Cannot delete first product period effect"));
              }

              return this.repository.deletePeriodEffectById(periodEffectEntity.getId());
            });
  }

  @Transactional
  public Mono<Void> deleteProduct(final UUID productId) {
    return this.findById(productId)
        .map(ProductEntity::getId)
        .flatMap(this.repository::deleteProduct);
  }

  @Transactional
  public Mono<Void> updatePeriodEffect(
      final UUID periodEffectId, final ProductPeriodEffectDto productPeriodEffectDto) {
    return this.findPeriodEffectById(periodEffectId)
        .doOnNext(updatePeriodEffectEntity(productPeriodEffectDto))
        .flatMap(this.repository::updateProductPeriodEffect)
        .then();
  }

  @Transactional
  public Mono<ProductPeriodEffectEntity> createPeriodEffect(
      final ProductPeriodEffectEntity entity) {
    return this.validateProductExists(entity.getProductId())
        .then(this.repository.createPeriodEffect(entity));
  }

  private Mono<Void> validateProductExists(final UUID productId) {
    return this.repository
        .exists(productId)
        .filter(Boolean.TRUE::equals)
        .switchIfEmpty(Mono.error(() -> new ProductNotFoundException(productId)))
        .then();
  }

  private Consumer<ProductPeriodEffectEntity> updatePeriodEffectEntity(
      final ProductPeriodEffectDto productPeriodEffectDto) {
    return periodEffectEntity -> {
      periodEffectEntity.setName(productPeriodEffectDto.getName());
      periodEffectEntity.setPrice(productPeriodEffectDto.getPrice());
      periodEffectEntity.setDiscount(productPeriodEffectDto.getDiscount());
    };
  }
}
