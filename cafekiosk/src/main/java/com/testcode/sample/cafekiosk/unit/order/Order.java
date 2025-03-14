package com.testcode.sample.cafekiosk.unit.order;

import com.testcode.sample.cafekiosk.unit.beverage.Beverage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Order {

  private LocalDateTime orderDateTime;
  private List<Beverage> beverages;
}
