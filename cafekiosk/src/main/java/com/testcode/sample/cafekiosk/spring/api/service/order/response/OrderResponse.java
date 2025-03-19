package com.testcode.sample.cafekiosk.spring.api.service.order.response;

import com.testcode.sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.testcode.sample.cafekiosk.spring.domain.order.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

  private Long id;
  private int totalPrice;
  private LocalDateTime registerDateTime;
  private List<ProductResponse> orderProducts;

  @Builder
  private OrderResponse(Long id, int totalPrice, LocalDateTime registerDateTime,
      List<ProductResponse> orderProducts) {
    this.id = id;
    this.totalPrice = totalPrice;
    this.registerDateTime = registerDateTime;
    this.orderProducts = orderProducts;
  }

  public static OrderResponse of(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .totalPrice(order.getTotalPrice())
        .registerDateTime(order.getRegisterDateTime())
        .orderProducts(order.getOrderProducts().stream()
            .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
            .toList()
        )
        .build();
  }
}
