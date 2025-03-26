package com.testcode.sample.cafekiosk.spring.api.service.product;

import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateServiceRequest {

  ProductType type;
  ProductSellingStatus sellingStatus;
  String name;
  int price;

  @Builder
  private ProductCreateServiceRequest(String productNumber, ProductType type,
      ProductSellingStatus sellingStatus, String name, int price) {
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
