package com.testcode.sample.cafekiosk.unit;

import com.testcode.sample.cafekiosk.unit.beverage.Beverage;
import com.testcode.sample.cafekiosk.unit.order.Order;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CafeKiosk {

  private static final LocalTime SHOP_OPEN_TIME = LocalTime.of(10, 0);
  private static final LocalTime SHOT_CLOSE_TIME = LocalTime.of(22, 0);

  private final List<Beverage> beverages = new ArrayList<>();

  public void add(Beverage beverage) {
    beverages.add(beverage);
  }

  public void add(Beverage beverage, int count) {
    if(count <= 0) {
      throw new IllegalArgumentException("count must be greater than 0");
    }
    for(int i = 0; i < count; i++) {
      beverages.add(beverage);
    }

  }

  public void remove(Beverage beverage) {
    beverages.remove(beverage);
  }

  public void clear() {
    beverages.clear();
  }

  public int calculateTotalPrice() {
    int totalPrice = 0;
    for(Beverage beverage : beverages) {
      totalPrice += beverage.getPrice();
    }

    return totalPrice;
  }

  public Order createOrder() {
    LocalDateTime now = LocalDateTime.now();
    LocalTime currentTime = now.toLocalTime();

    if(currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOT_CLOSE_TIME)) {
      throw new IllegalArgumentException("Current time is before the shop open time");
    }

    return new Order(now, beverages);
  }

  public Order createOrderWithLocalTime(LocalDateTime now) {
    LocalTime currentTime = now.toLocalTime();

    if(currentTime.isBefore(SHOP_OPEN_TIME) || currentTime.isAfter(SHOT_CLOSE_TIME)) {
      throw new IllegalArgumentException("Current time is before the shop open time");
    }

    return new Order(now, beverages);
  }
}
