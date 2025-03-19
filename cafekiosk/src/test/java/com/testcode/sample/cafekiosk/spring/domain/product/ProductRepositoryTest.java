package com.testcode.sample.cafekiosk.spring.domain.product;

import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest // springBoot 보다 가볍게 jpa 관련 bean 만 띄움(조금더 빠름). 하지만 잘 안씀
class ProductRepositoryTest {

  @Autowired
  private ProductRepository productRepository;

  @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
  @Test
  void findAllBySellingStatusIn() {
    // given
    Product product = Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();
    Product product2 = Product.builder()
        .productNumber("002")
        .type(HANDMADE)
        .sellingStatus(HOLD)
        .name("카페라떼")
        .price(4500)
        .build();
    Product product3 = Product.builder()
        .productNumber("003")
        .type(HANDMADE)
        .sellingStatus(STOP_SELLING)
        .name("팥빙수")
        .price(7000)
        .build();
    productRepository.saveAll(List.of(product, product2, product3));

    // when
    List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

    // then
    assertThat(products).hasSize(2)
        .extracting("productNumber", "name", "sellingStatus")
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );
  }


  @DisplayName("원하는 상품번호를 가진 상품들을 조회한다.")
  @Test
  void findAllByProductNumberIn() {
    // given
    Product product = Product.builder()
        .productNumber("001")
        .type(HANDMADE)
        .sellingStatus(SELLING)
        .name("아메리카노")
        .price(4000)
        .build();
    Product product2 = Product.builder()
        .productNumber("002")
        .type(HANDMADE)
        .sellingStatus(HOLD)
        .name("카페라떼")
        .price(4500)
        .build();
    Product product3 = Product.builder()
        .productNumber("003")
        .type(HANDMADE)
        .sellingStatus(STOP_SELLING)
        .name("팥빙수")
        .price(7000)
        .build();
    productRepository.saveAll(List.of(product, product2, product3));

    // when
    List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

    // then
    assertThat(products).hasSize(2)
        .extracting("productNumber", "name", "sellingStatus")
        .containsExactlyInAnyOrder(
            tuple("001", "아메리카노", SELLING),
            tuple("002", "카페라떼", HOLD)
        );
  }
}