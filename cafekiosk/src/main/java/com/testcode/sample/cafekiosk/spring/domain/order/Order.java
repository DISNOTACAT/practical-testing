package com.testcode.sample.cafekiosk.spring.domain.order;

import com.testcode.sample.cafekiosk.spring.domain.BaseEntity;
import com.testcode.sample.cafekiosk.spring.domain.orderProduct.OrderProduct;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Getter
public class Order extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  private int totalPrice;
  private LocalDateTime registerDateTime;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  private Order(List<Product> products, LocalDateTime registerDateTime){
    this.orderStatus = OrderStatus.INIT;
    this.totalPrice = calculateTotalPrice(products);
    this.registerDateTime = registerDateTime;
    this.orderProducts = products.stream()
        .map(product -> new OrderProduct(this, product))
        .toList();
  }

  private int calculateTotalPrice(List<Product> products) {
    return products.stream()
        .mapToInt(Product::getPrice).sum();
  }

  public static Order create(List<Product> products,
      LocalDateTime registerDateTime) {
    return new Order(products, registerDateTime);
  }
}
