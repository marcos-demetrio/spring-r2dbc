package com.example.r2dbc.api.v1;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.example.r2dbc.api.config.PageFilter;
import com.example.r2dbc.domain.ProductService;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("product")
public class ProductController {

  @Autowired private ProductService service;

  @ResponseStatus(value = OK)
  @GetMapping("all")
  public Mono<Page<ProductResponse>> findAll(@Valid final PageFilter pageFilter) {
    return this.service.findAll(pageFilter.toPageable()).map(ProductMapper::toResponse);
  }

  @ResponseStatus(value = OK)
  @GetMapping("{productId}")
  public Mono<ProductResponse> findById(@PathVariable("productId") final UUID productId) {
    return this.service.findById(productId).map(ProductMapper::toResponse);
  }

  @ResponseStatus(value = OK)
  @GetMapping("period-effect/{periodEffectId}")
  public Mono<ProductPeriodEffectResponse> findPeriodEffectById(
      @PathVariable("periodEffectId") final UUID periodEffectId) {
    return this.service.findPeriodEffectById(periodEffectId).map(ProductMapper::toResponse);
  }

  @ResponseStatus(value = CREATED)
  @PostMapping
  public Mono<ProductResponse> create(
      @Valid @RequestBody final ProductPeriodEffectCreateRequest request) {
    return this.service.create(ProductMapper.toEntity(request)).map(ProductMapper::toResponse);
  }

  @ResponseStatus(value = OK)
  @PutMapping("period-effect/{periodEffectId}")
  public Mono<Void> update(
      @PathVariable("periodEffectId") final UUID periodEffectId,
      @Valid @RequestBody final ProductPeriodEffectUpdateRequest request) {
    return this.service.updatePeriodEffect(periodEffectId, ProductMapper.toDto(request));
  }

  @ResponseStatus(value = OK)
  @PostMapping("{productId}/period-effect")
  public Mono<ProductPeriodEffectResponse> createPeriodEffect(
      @PathVariable("productId") final UUID productId,
      @Valid @RequestBody final ProductPeriodEffectCreateRequest request) {
    return this.service
        .createPeriodEffect(ProductMapper.toProductPeriodEffectEntity(productId, request))
        .map(ProductMapper::toResponse);
  }

  @ResponseStatus(value = OK)
  @DeleteMapping("period-effect/{periodEffectId}")
  public Mono<Void> deletePeriodEffect(@PathVariable("periodEffectId") final UUID periodEffectId) {
    return this.service.deletePeriodEffect(periodEffectId);
  }

  @ResponseStatus(value = OK)
  @DeleteMapping("{productId}")
  public Mono<Void> delete(@PathVariable("productId") final UUID productId) {
    return this.service.deleteProduct(productId);
  }
}
