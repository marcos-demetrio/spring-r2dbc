package com.example.r2dbc.domain;

import static org.springframework.data.relational.core.query.Criteria.empty;
import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import java.util.HashSet;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
  private static final String PRODUCT_ID = "product_id";
  private static final String PRODUCT_PERIOD_EFFECT_ID = "product_period_effect_id";

  @Autowired private R2dbcEntityTemplate template;

  @Override
  public Mono<Long> count() {
    return this.template.select(ProductPeriodEffectEntity.class).count();
  }

  @Override
  public Mono<Boolean> exists(UUID productId) {
    return this.template.exists(query(where(PRODUCT_ID).is(productId)), ProductEntity.class);
  }

  @Override
  public Flux<ProductEntity> findAllBy(Pageable pageable) {
    return this.template
        .select(ProductEntity.class)
        .matching(query(empty()).limit(pageable.getPageSize()).offset(pageable.getOffset()))
        .all()
        .flatMap(this::findPeriodEffectByProduct);
  }

  @Override
  public Mono<ProductEntity> findById(UUID productId) {
    return this.template
        .select(ProductEntity.class)
        .matching(query(where(PRODUCT_ID).is(productId)))
        .one()
        .flatMap(this::findPeriodEffectByProduct);
  }

  private Mono<ProductEntity> findPeriodEffectByProduct(ProductEntity entity) {
    return this.findPeriodEffectByProductId(entity.getId())
        .collectList()
        .map(
            periodEffects -> {
              entity.setPeriodEffects(new HashSet<>(periodEffects));
              return entity;
            });
  }

  @Override
  public Mono<UUID> findFirstPeriodEffectIdByProductId(UUID productId) {
    var query =
        "SELECT product_period_effect_id FROM public.product_period_effect "
            + "where product_Id = :productId "
            + "order by period_effect asc limit 1";

    return this.template
        .getDatabaseClient()
        .sql(query)
        .bind("productId", productId)
        .map(row -> row.get(PRODUCT_PERIOD_EFFECT_ID, UUID.class))
        .one();
  }

  @Override
  public Mono<ProductPeriodEffectEntity> findPeriodEffectById(UUID periodEffectId) {
    return this.template
        .select(ProductPeriodEffectEntity.class)
        .matching(query(where(PRODUCT_PERIOD_EFFECT_ID).is(periodEffectId)))
        .one();
  }

  @Override
  public Mono<ProductPeriodEffectEntity> createPeriodEffect(ProductPeriodEffectEntity entity) {
    return this.template.insert(entity);
  }

  private Flux<ProductPeriodEffectEntity> findPeriodEffectByProductId(UUID productId) {
    return this.template
        .select(ProductPeriodEffectEntity.class)
        .matching(query(where(PRODUCT_ID).is(productId)))
        .all();
  }

  @Override
  public Mono<ProductEntity> saveProduct(ProductEntity entityToSave) {
    return this.template.insert(entityToSave).flatMap(this::saveProductPeriodEffect);
  }

  @Override
  public Mono<Void> updateProductPeriodEffect(ProductPeriodEffectEntity entityToUpdate) {
    return this.template.update(entityToUpdate).then();
  }

  @Override
  public Mono<Void> deleteProduct(UUID productId) {
    return this.template
        .delete(ProductEntity.class)
        .matching(query(where(PRODUCT_ID).is(productId)))
        .all()
        .then();
  }

  @Override
  public Mono<Void> deletePeriodEffectById(UUID periodEffectId) {
    return this.template
        .delete(ProductPeriodEffectEntity.class)
        .matching(query(where(PRODUCT_PERIOD_EFFECT_ID).is(periodEffectId)))
        .all()
        .then();
  }

  private Mono<ProductEntity> saveProductPeriodEffect(ProductEntity savedEntity) {
    return Flux.fromIterable(savedEntity.getPeriodEffects())
        .doOnNext(entity -> entity.setProductId(savedEntity.getId()))
        .flatMap(this.template::insert)
        .then()
        .thenReturn(savedEntity);
  }
}
