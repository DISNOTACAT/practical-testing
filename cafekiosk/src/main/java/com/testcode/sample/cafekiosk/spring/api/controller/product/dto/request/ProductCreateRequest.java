package com.testcode.sample.cafekiosk.spring.api.controller.product.dto.request;

import com.testcode.sample.cafekiosk.spring.api.service.product.ProductCreateServiceRequest;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

  @NotNull(message = "상품 타입은 필수입니다.")
  ProductType type;
  @NotNull(message = "상품 판매상태는 필수입니다.")
  ProductSellingStatus sellingStatus;
  @NotBlank(message = "상품 이름은 필수입니다.")
  String name;
  @Positive(message = "상품 가격은 양수여야 합니다.")
  int price;

  @Builder
  private ProductCreateRequest(String productNumber, ProductType type,
      ProductSellingStatus sellingStatus, String name, int price) {
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public ProductCreateServiceRequest toService() {
    return ProductCreateServiceRequest.builder()
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }
}
