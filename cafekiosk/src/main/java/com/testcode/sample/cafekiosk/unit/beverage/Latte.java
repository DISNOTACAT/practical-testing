package com.testcode.sample.cafekiosk.unit.beverage;

public class Latte implements Beverage{

  @Override
  public String getName() {
    return "카페라떼";
  }

  @Override
  public int getPrice() {
    return 4500;
  }
}
