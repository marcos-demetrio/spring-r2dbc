package com.example.r2dbc.api.v1;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@TestMethodOrder(OrderAnnotation.class)
class ProductControllerTest {
  private static ProductResponse productResponse;
  private static ProductPeriodEffectResponse productPeriodEffectResponse;
  private static UUID periodEffectId_1;
  private static UUID periodEffectId_2;

  private final String BASE_URI = "/product";
  private final String BASE_URI_WITH_ID = "/product/{productId}";

  @Autowired WebTestClient webTestClient;

  @Test
  @Order(10)
  void shouldCreateProductAndFirstPeriodEffect() {
    var request =
        ProductPeriodEffectCreateRequest.create(LocalDateTime.now(), "IT Product 1", TEN, ONE);

    productResponse =
        webTestClient
            .post()
            .uri(BASE_URI)
            .contentType(APPLICATION_JSON)
            .body(Mono.just(request), ProductPeriodEffectCreateRequest.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProductResponse.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(productResponse);
    assertNotNull(productResponse.getId());
    assertNotNull(productResponse.getRegistrationDate());
    assertNotNull(productResponse.getPeriodEffects());
    assertFalse(productResponse.getPeriodEffects().isEmpty());

    periodEffectId_1 = productResponse.getPeriodEffects().stream().findFirst().get().getId();
  }

  @Test
  @Order(20)
  void shouldCreateSecondProductPeriodEffect() {
    var request =
        ProductPeriodEffectCreateRequest.create(LocalDateTime.now(), "IT Product 1", TEN, ONE);

    productPeriodEffectResponse =
        webTestClient
            .post()
            .uri(BASE_URI_WITH_ID.concat("/period-effect"), productResponse.getId())
            .contentType(APPLICATION_JSON)
            .body(Mono.just(request), ProductPeriodEffectCreateRequest.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(ProductPeriodEffectResponse.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(productPeriodEffectResponse);
    assertNotNull(productPeriodEffectResponse.getId());

    periodEffectId_2 = productPeriodEffectResponse.getId();
  }

  @Test
  @Order(30)
  void shouldFindProductPeriodEffectById() {
    var response =
        webTestClient
            .get()
            .uri(
                BASE_URI.concat("/period-effect/{periodEffectId}"),
                productPeriodEffectResponse.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ProductPeriodEffectResponse.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(response);
    assertEquals("IT Product 1", response.getName());
  }

  @Test
  @Order(35)
  void shouldFindProductById() {
    var response =
        webTestClient
            .get()
            .uri(BASE_URI.concat("/{productId}"), productResponse.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(ProductResponse.class)
            .returnResult()
            .getResponseBody();

    assertNotNull(response);
  }

  @Test
  @Order(40)
  void shouldUpdateProductPeriodEffect() {
    var request = ProductPeriodEffectUpdateRequest.create("NEW Name", TEN, ZERO);

    webTestClient
        .put()
        .uri(
            BASE_URI.concat("/period-effect/{periodEffectId}"), productPeriodEffectResponse.getId())
        .contentType(APPLICATION_JSON)
        .body(Mono.just(request), ProductPeriodEffectUpdateRequest.class)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  @Order(50)
  void shouldDeleteSecondProductPeriodEffect() {
    webTestClient
        .delete()
        .uri(BASE_URI.concat("/period-effect/{periodEffectId}"), periodEffectId_2)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  @Order(60)
  void shouldNotDeleteFirstProductPeriodEffect() {
    webTestClient
        .delete()
        .uri(BASE_URI.concat("/period-effect/{periodEffectId}"), periodEffectId_1)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  @Order(70)
  void shouldDeleteProduct() {
    webTestClient
        .delete()
        .uri(BASE_URI.concat("/{productId}"), productResponse.getId())
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  @Order(80)
  void shouldReturnNotFoundProductPeriodEffectById() {
    webTestClient
        .get()
        .uri(BASE_URI.concat("/period-effect/{periodEffectId}"), UUID.randomUUID())
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  @Order(90)
  void shouldReturnNotFoundWhenDeleteProductPeriodEffect() {
    webTestClient
        .delete()
        .uri(BASE_URI.concat("/period-effect/{periodEffectId}"), UUID.randomUUID())
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  @Order(100)
  void shouldReturnNotFoundWhenUpdateProductPeriodEffect() {
    var request = ProductPeriodEffectUpdateRequest.create("NEW Name", TEN, ZERO);

    webTestClient
        .put()
        .uri(BASE_URI.concat("/period-effect/{periodEffectId}"), UUID.randomUUID())
        .contentType(APPLICATION_JSON)
        .body(Mono.just(request), ProductPeriodEffectUpdateRequest.class)
        .exchange()
        .expectStatus()
        .isNotFound();
  }

  @Test
  @Order(110)
  void shouldNotFindProductById() {
    webTestClient
        .get()
        .uri(BASE_URI.concat("/{productId}"), UUID.randomUUID())
        .exchange()
        .expectStatus()
        .isNotFound();
  }
}
