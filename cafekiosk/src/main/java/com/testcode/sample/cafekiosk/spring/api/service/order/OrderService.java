package com.testcode.sample.cafekiosk.spring.api.service.order;

import com.testcode.sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.testcode.sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import com.testcode.sample.cafekiosk.spring.domain.order.Order;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
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

  public OrderResponse createOrder(OrderCreateRequest request,
      LocalDateTime registerDateTime) {
    List<String> productNumbers = request.getProductNumbers();
    List<Product> products = findProductsBy(
        productNumbers);

    Order order = Order.create(products, registerDateTime);
    Order savedOrder = orderRepository.save(order);
    return OrderResponse.of(savedOrder);
  }

  private List<Product> findProductsBy(List<String> productNumbers) {
    List<Product> products = productRepository.findAllByProductNumberIn(  // in 으로 조회하기 때문에 중복된 request 를 담지 못함.
        productNumbers);

    Map<String, Product> productMap = products.stream()
        .collect(Collectors.toMap(Product::getProductNumber, p -> p));

    List<Product> duplicateProducts = productNumbers.stream() // 번호를 별도로 저장하여, 중복 상품 정보까지 하나씩 리스트화
        .map(productMap::get)
        .toList();
    return duplicateProducts;
  }
}
