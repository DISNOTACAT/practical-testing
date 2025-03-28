package com.testcode.sample.cafekiosk.spring.domain.stock;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String productNumber;

  private int quantity;

  @Builder
  private Stock(String productNumber, int quantity) {
    this.productNumber = productNumber;
    this.quantity = quantity;
  }

  public static Stock create(String number, int i) {
    return Stock.builder()
        .productNumber(number)
        .quantity(i).build();
  }

  public boolean isQuantityLessThan(int quantity) {
    return this.quantity < quantity;
  }

  public void deductQuantity(int quantity) {

    if(isQuantityLessThan(quantity)) {
      throw new IllegalArgumentException("차감할 재고 수량이 없습니다.");
    }

    this.quantity -= quantity;
  }
}
