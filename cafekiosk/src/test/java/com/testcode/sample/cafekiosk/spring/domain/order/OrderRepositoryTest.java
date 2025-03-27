package com.testcode.sample.cafekiosk.spring.domain.order;

import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class OrderRepositoryTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @DisplayName("")
  @Test
  void findOrdersByOrderStatusINIT() {
    // given
    LocalDateTime givenStartDateTime = LocalDateTime.now();
    LocalDateTime givenEndDateTime = givenStartDateTime.plusDays(1);
    OrderStatus givenOrderStatus = OrderStatus.INIT;

    Order order = createOrder(givenStartDateTime);
    orderRepository.save(order);

    // when
    List<Order> orders = orderRepository.findOrdersBy(givenStartDateTime, givenEndDateTime, givenOrderStatus);

    // then
    assertThat(orders).hasSize(1)
        .extracting("orderStatus", "registerDateTime")
        .containsExactlyInAnyOrder(
            tuple(givenOrderStatus, givenStartDateTime)
        )
    ;
  }

  private Order createOrder(LocalDateTime givenRegisterDateTime) {
    Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
    Product product2 = createProduct("002", HANDMADE, SELLING, "카페라떼", 4500);
    Product product3 = createProduct("003", HANDMADE, SELLING, "팥빙수", 7000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    return Order.create(products, givenRegisterDateTime);
  }

  private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
    return Product.builder()
        .productNumber(productNumber)
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }

}