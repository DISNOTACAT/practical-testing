package com.testcode.sample.cafekiosk.spring.domain.order;

import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static org.assertj.core.api.Assertions.assertThat;

import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderTest {

  @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
  @Test
  void calculateTotalPrice() {
    // given
    List<Product> products = List.of(
        createProdcut("001", 1000),
        createProdcut("002", 2000)
    );

    // when
    Order order = Order.create(products, LocalDateTime.now());

    // then
    assertThat(order.getTotalPrice()).isEqualTo(3000);
  }

  @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
  @Test
  void initOrder() {
    // given
    List<Product> products = List.of(
        createProdcut("001", 1000),
        createProdcut("002", 2000)
    );

    // when
    Order order = Order.create(products, LocalDateTime.now());

    // then
    assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
  }

  @DisplayName("주문 생성 시 등록 시간을 기록한다.")
  @Test
  void registeredDateTime() {
    // given
    LocalDateTime registerDateTime = LocalDateTime.now();
    List<Product> products = List.of(
        createProdcut("001", 1000),
        createProdcut("002", 2000)
    );

    // when
    Order order = Order.create(products, registerDateTime);

    // then
    assertThat(order.getRegisterDateTime()).isEqualTo(registerDateTime);
  }

  private Product createProdcut(String prodcutNumber, int price) {
    return Product.builder()
        .productNumber(prodcutNumber)
        .type(ProductType.HANDMADE)
        .sellingStatus(SELLING)
        .name("메뉴이름")
        .price(price)
        .build();
  }
}