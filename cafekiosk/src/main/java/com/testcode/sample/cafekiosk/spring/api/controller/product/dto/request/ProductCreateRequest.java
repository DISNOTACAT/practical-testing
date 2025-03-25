package com.testcode.sample.cafekiosk.spring.api.controller.product.dto.request;

import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateRequest {

  String productNumber;
  ProductType type;
  ProductSellingStatus sellingStatus;
  String name;
  int price;

  @Builder
  private ProductCreateRequest(String productNumber, ProductType type,
      ProductSellingStatus sellingStatus, String name, int price) {
    this.productNumber = productNumber;
    this.type = type;
    this.sellingStatus = sellingStatus;
    this.name = name;
    this.price = price;
  }

  public Product toEntity(String nextProductNumber) {
    return Product.builder()
        .productNumber(nextProductNumber)
        .type(type)
        .sellingStatus(sellingStatus)
        .name(name)
        .price(price)
        .build();
  }
}
