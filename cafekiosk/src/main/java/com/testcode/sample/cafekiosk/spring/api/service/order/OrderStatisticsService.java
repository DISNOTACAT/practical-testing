package com.testcode.sample.cafekiosk.spring.api.service.order;

import com.testcode.sample.cafekiosk.spring.api.service.mail.MailService;
import com.testcode.sample.cafekiosk.spring.domain.order.Order;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatisticsService {

  private final OrderRepository orderRepository;
  private final MailService mailService;

  public boolean sendOrderStatisticsMail(LocalDate orderDate, String email) {

    List<Order> orders = orderRepository.findOrdersBy(
        orderDate.atStartOfDay(),
        orderDate.plusDays(1).atStartOfDay(),
        OrderStatus.PAYMENT_COMPLETED
    );

    int totalAmount = orders.stream()
        .mapToInt(Order::getTotalPrice)
        .sum();

    boolean result = mailService.sendMail("no-reply@test.com",
        email,
        String.format("[매출통계] %s", orderDate),
        String.format("총 매출 합계는 %s원입니다.", totalAmount));

    if(!result) {
      throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
    }
    return true;
  }
}
