package com.testcode.sample.cafekiosk.unit;

import com.testcode.sample.cafekiosk.unit.beverage.Americano;
import com.testcode.sample.cafekiosk.unit.beverage.Latte;
import com.testcode.sample.cafekiosk.unit.order.Order;
import java.time.LocalDateTime;

public class CafeKioskRunner {

  public static void main(String[] args) {
    CafeKiosk cafeKiosk = new CafeKiosk();
    cafeKiosk.add(new Americano());
    System.out.println(">>> 아메리카노 추가");

    cafeKiosk.add(new Latte());
    System.out.println(">>> 카페라떼 추가");

    int totalPrice = cafeKiosk.calculateTotalPrice();
    System.out.println("총 주문 가격 = " + totalPrice);

    Order order = cafeKiosk.createOrderWithLocalTime(LocalDateTime.now());
  }

}
