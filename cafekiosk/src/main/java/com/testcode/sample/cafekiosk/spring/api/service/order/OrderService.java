package com.testcode.sample.cafekiosk.spring.api.service.order;

import com.testcode.sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.testcode.sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.testcode.sample.cafekiosk.spring.domain.order.Order;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import com.testcode.sample.cafekiosk.spring.domain.stock.Stock;
import com.testcode.sample.cafekiosk.spring.domain.stock.StockRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;
  private final StockRepository stockRepository;

  public OrderResponse createOrder(OrderCreateRequest request,
      LocalDateTime registerDateTime) {
    List<String> productNumbers = request.getProductNumbers();
    List<Product> products = findProductsBy(
        productNumbers);

    // 재고 차감
    List<String> stockProductNumber = products.stream()
        .filter(product -> ProductType.containsStockType(product.getType()))
        .map(Product::getProductNumber)
        .toList();
    List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumber);
    Map<String, Stock> stockMap = stocks.stream()
        .collect(Collectors.toMap(Stock::getProductNumber, s -> s));

    Map<String, Long> productCountingMap = stockProductNumber.stream()
        .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

    for(String stockNumber : stockProductNumber) {
        Stock stock = stockMap.get(stockNumber);
        int quantity = productCountingMap.get(stockNumber).intValue();

        if(stock.isQuantityLessThan(quantity)) {
          throw new IllegalArgumentException("재고가 부족한 상품이 있습니다");
        }

        stock.deductQuantity(quantity);
    }

    Order order = Order.create(products, registerDateTime);
    Order savedOrder = orderRepository.save(order);
    return OrderResponse.of(savedOrder);
  }


  private List<Product> findProductsBy(List<String> productNumbers) {
    List<Product> products = productRepository.findAllByProductNumberIn(  // in 으로 조회하기 때문에 중복된 request 를 담지 못함.
        productNumbers);

    Map<String, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getProductNumber, p -> p));

    return productNumbers.stream() // 번호를 별도로 저장하여, 중복 상품 정보까지 하나씩 리스트화
        .map(productMap::get)
        .toList();
  }
}
