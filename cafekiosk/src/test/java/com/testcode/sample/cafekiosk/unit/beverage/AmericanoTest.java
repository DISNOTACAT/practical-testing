package com.testcode.sample.cafekiosk.unit.beverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AmericanoTest {

  @Test
  void getName() {
    Americano americano = new Americano();

    assertEquals("아메리카노", americano.getName()); // Junit
    assertThat("아메리카노").isEqualTo(americano.getName()); // assertJ
  }

  @Test
  void getPrice() {
    Americano americano = new Americano();
    assertThat(americano.getPrice()).isGreaterThan(0);
    assertThat(4000).isEqualTo(americano.getPrice());
  }

}