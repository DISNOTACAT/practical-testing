package com.testcode.sample.cafekiosk.spring.api.service.order;

import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.testcode.sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.testcode.sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderProductRepository orderProductRepository;

  @Autowired
  private OrderService orderService;


  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
  }

  @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrder() {
    // given
    LocalDateTime registerDateTime = LocalDateTime.now();

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 4000);
    Product product3 = createProduct(HANDMADE, "003", 6000);
    productRepository.saveAll(List.of(product1, product2, product3));

    OrderCreateRequest request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "002"))
        .build();

    // when
    OrderResponse response = orderService.createOrder(request,
        registerDateTime);

    // then
    assertThat(response.getId()).isNotNull();
    assertThat(response)
        .extracting("registerDateTime", "totalPrice")
        .contains(registerDateTime, 5000);
    assertThat(response.getOrderProducts()).hasSize(2)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("002", 4000)
        );
  }


  @DisplayName("중복되는 상품번호 리스트로 주문할 수 있다.")
  @Test
  void createOrderWithDuplicateProductNumbers() {
    // given
    LocalDateTime registerDateTime = LocalDateTime.now();

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 4000);
    Product product3 = createProduct(HANDMADE, "003", 6000);
    productRepository.saveAll(List.of(product1, product2, product3));

    OrderCreateRequest request = OrderCreateRequest.builder()
        .productNumbers(List.of("001", "001"))
        .build();

    // when
    OrderResponse response = orderService.createOrder(request,
        registerDateTime);

    // then
    assertThat(response.getId()).isNotNull();
    assertThat(response)
        .extracting("registerDateTime", "totalPrice")
        .contains(registerDateTime, 2000);
    assertThat(response.getOrderProducts()).hasSize(2)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("001", 1000)
        );
  }

  private Product createProduct(ProductType type, String productNumber, int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(type)
        .sellingStatus(SELLING)
        .name("메뉴이름")
        .price(price)
        .build();
  }

}