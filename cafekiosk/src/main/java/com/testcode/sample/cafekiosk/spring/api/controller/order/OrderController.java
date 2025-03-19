package com.testcode.sample.cafekiosk.spring.api.controller.order;

import com.testcode.sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.testcode.sample.cafekiosk.spring.api.service.order.OrderService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private OrderService orderService;

  @PostMapping("/api/v1/orders/new")
  public void createOrder(@RequestBody OrderCreateRequest request) {

    LocalDateTime registerDateTime = LocalDateTime.now();
    orderService.createOrder(request, registerDateTime);
  }
}
