package com.testcode.sample.cafekiosk.spring.api.service.order;

import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.testcode.sample.cafekiosk.spring.api.client.mail.MailSendClient;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import com.testcode.sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;
import com.testcode.sample.cafekiosk.spring.domain.order.Order;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderStatus;
import com.testcode.sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class OrderStatisticsServiceTest {

  @Autowired
  private OrderStatisticsService orderStatisticsService;

  @Autowired
  private OrderProductRepository orderProductRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private MailSendHistoryRepository mailSendHistoryRepository;

  @MockitoBean // mock 정의를 하고 값을 가정한다.
  private MailSendClient mailSendClient;


  @AfterEach
  void tearDown() {
    orderProductRepository.deleteAllInBatch();
    orderRepository.deleteAllInBatch();
    productRepository.deleteAllInBatch();
    mailSendHistoryRepository.deleteAllInBatch();
  }

  @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
  @Test
  void sendOrderStatisticsMail() {
    // given
    LocalDateTime now = LocalDateTime.of(2025,3,27, 12, 0);

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 3000);
    Product product3 = createProduct(HANDMADE, "003", 6000);
    List<Product> products = List.of(product1, product2, product3);
    productRepository.saveAll(products);

    Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2025,3,26, 12, 59, 59), products);
    Order order2 = createPaymentCompletedOrder(now, products);
    Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2025,3,27, 12, 59, 59), products);
    Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2025,3,28, 0, 0, 0), products);

    // stubbing
    Mockito.when(mailSendClient.sendMail(any(String.class), anyString(), anyString(), anyString()))
        .thenReturn(true);

    // when
    boolean result = orderStatisticsService.sendOrderStatisticsMail(LocalDate.of(2025,3,27), "test@test.com");

    // then
    assertThat(result).isTrue();

    List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
    assertThat(histories).hasSize(1)
        .extracting("content")
        .contains("총 매출 합계는 20000원입니다.")
    ;
  }

  private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
    Order order1 = Order.builder()
            .products(products)
            .orderStatus(OrderStatus.PAYMENT_COMPLETED)
            .registerDateTime(now)
            .build();

    return orderRepository.save(order1);

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