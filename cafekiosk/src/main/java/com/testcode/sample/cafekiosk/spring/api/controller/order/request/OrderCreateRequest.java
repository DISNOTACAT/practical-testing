package com.testcode.sample.cafekiosk.spring.api.controller.order.request;

import com.testcode.sample.cafekiosk.spring.api.service.order.request.OrderCreateServiceRequest;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

  @NotEmpty(message = "상품 번호 리스트는 필수입니다.")
  private List<String> productNumbers;

  @Builder
  private OrderCreateRequest(List<String> productNumbers) {
    this.productNumbers = productNumbers;
  }

  public OrderCreateServiceRequest toService() {
    return OrderCreateServiceRequest.builder()
        .productNumbers(productNumbers)
        .build();
  }
}
