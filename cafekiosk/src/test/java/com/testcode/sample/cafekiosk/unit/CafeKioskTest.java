package com.testcode.sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.testcode.sample.cafekiosk.unit.beverage.Americano;
import com.testcode.sample.cafekiosk.unit.beverage.Latte;
import com.testcode.sample.cafekiosk.unit.order.Order;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class CafeKioskTest {

  @Test
  void add_manual_test() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    cafeKiosk.add(new Americano());

    System.out.println(">> count : ");
  }

  @Test
  void add() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    cafeKiosk.add(new Americano());

    assertThat(cafeKiosk.getBeverages()).hasSize(1);
    assertThat(cafeKiosk.getBeverages().get(0).getName()).contains("아메리카노");
  }

  @Test
  void addSeveralBeverages() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    cafeKiosk.add(americano, 2);

    assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
    assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
  }

  @Test
  void addZeroBeverages() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    cafeKiosk.add(americano, 0);

    assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("count must be greater than 0");
  }

  @Test
  void remove() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);
    assertThat(cafeKiosk.getBeverages()).hasSize(1);

    cafeKiosk.remove(americano);
    assertThat(cafeKiosk.getBeverages()).isEmpty();

  }

  @Test
  void clear() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    Latte latte = new Latte();

    cafeKiosk.add(americano);
    cafeKiosk.add(latte);
    assertThat(cafeKiosk.getBeverages()).hasSize(2);

    cafeKiosk.clear();
    assertThat(cafeKiosk.getBeverages()).isEmpty();

  }

  @Test
  void createOrder() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);

    Order order = cafeKiosk.createOrder();
    assertThat(order.getBeverages()).hasSize(1);
    assertThat(order.getBeverages().get(0).getName()).contains("아메리카노");
  }

  @Test
  void createOrderWithCurrentTime() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();

    cafeKiosk.add(americano);

    Order order = cafeKiosk.createOrderWithLocalTime(LocalDateTime.of(2025,1,17,10,0));
    assertThat(order.getBeverages()).hasSize(1);
    assertThat(order.getBeverages().get(0).getName()).contains("아메리카노");
  }

  @Test
  void createOrderWithOutOfBusinessTime() {
    CafeKiosk cafeKiosk = new CafeKiosk();
    Americano americano = new Americano();
    cafeKiosk.add(americano);

    assertThatThrownBy(() -> cafeKiosk.createOrderWithLocalTime(LocalDateTime.of(2025,1,17,9,59)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Current time is before the shop open time");
  }


}