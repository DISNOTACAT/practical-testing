package com.testcode.sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class ProductTypeTest {

  @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
  @Test
  void test() {
    // given
    ProductType givenType1 = ProductType.HANDMADE;
    ProductType givenType2 = ProductType.BAKERY;

    // when
    boolean result1 = ProductType.containsStockType(givenType1);
    boolean result2 = ProductType.containsStockType(givenType2);

    // then
    assertThat(result1).isFalse();
    assertThat(result2).isTrue();
  }

}