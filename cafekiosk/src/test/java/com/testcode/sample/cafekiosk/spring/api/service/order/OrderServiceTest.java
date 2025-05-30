package com.testcode.sample.cafekiosk.spring.api.service.order;

import static com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static com.testcode.sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.testcode.sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import com.testcode.sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.orderProduct.OrderProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import com.testcode.sample.cafekiosk.spring.domain.stock.Stock;
import com.testcode.sample.cafekiosk.spring.domain.stock.StockRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderProductRepository orderProductRepository;

  @Autowired
  private StockRepository stockRepository;

  @Autowired
  private OrderService orderService;


//  @AfterEach
//  void tearDown() {
//    orderProductRepository.deleteAllInBatch();
//    productRepository.deleteAllInBatch();
//    orderRepository.deleteAllInBatch();
//    stockRepository.deleteAllInBatch();
//  }

  @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrder() {
    // given
    LocalDateTime registerDateTime = LocalDateTime.now();

    Product product1 = createProduct(HANDMADE, "001", 1000);
    Product product2 = createProduct(HANDMADE, "002", 4000);
    Product product3 = createProduct(HANDMADE, "003", 6000);
    productRepository.saveAll(List.of(product1, product2, product3));

    OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
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

    OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
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

  @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
  @Test
  void createOrderWithStock() {
    // given
    LocalDateTime registerDateTime = LocalDateTime.now();

    Product product1 = createProduct(BOTTLE, "001", 1000);
    Product product2 = createProduct(BAKERY, "002", 4000);
    Product product3 = createProduct(HANDMADE, "003", 6000);
    productRepository.saveAll(List.of(product1, product2, product3));

    Stock stock1 = Stock.create("001", 2);
    Stock stock2 = Stock.create("002", 2);
    stockRepository.saveAll(List.of(stock1, stock2));

    OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
        .productNumbers(List.of("001", "001", "002",  "003"))
        .build();

    // when
    OrderResponse response = orderService.createOrder(request,
        registerDateTime);

    // then
    assertThat(response.getId()).isNotNull();
    assertThat(response)
        .extracting("registerDateTime", "totalPrice")
        .contains(registerDateTime, 12000);
    assertThat(response.getOrderProducts()).hasSize(4)
        .extracting("productNumber", "price")
        .containsExactlyInAnyOrder(
            tuple("001", 1000),
            tuple("001", 1000),
            tuple("002", 4000),
            tuple("003", 6000)
        );

    List<Stock> stocks = stockRepository.findAll();
    assertThat(stocks).hasSize(2)
        .extracting("productNumber", "quantity")
        .containsExactlyInAnyOrder(
            tuple("001", 0),
            tuple("002", 1)
        );
  }

  @DisplayName("재고가 부족한 상품으로 주문하려는 경우 예외가 발생한다.")
  @Test
  void createOrderWithNoStock() {
    // given
    LocalDateTime registerDateTime = LocalDateTime.now();

    Product product1 = createProduct(BOTTLE, "001", 1000);
    Product product2 = createProduct(BAKERY, "002", 4000);
    Product product3 = createProduct(HANDMADE, "003", 6000);
    productRepository.saveAll(List.of(product1, product2, product3));

    Stock stock1 = Stock.create("001", 2);
    Stock stock2 = Stock.create("002", 2);
    stock1.deductQuantity(1);
    stockRepository.saveAll(List.of(stock1, stock2));


    OrderCreateServiceRequest request = OrderCreateServiceRequest.builder()
        .productNumbers(List.of("001", "001", "002",  "003"))
        .build();

    // when
    // then
    assertThatThrownBy(
        () -> orderService.createOrder(request, registerDateTime))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("재고가 부족한 상품이 있습니다.");
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