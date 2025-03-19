package com.testcode.sample.cafekiosk.spring.api.service;

import com.testcode.sample.cafekiosk.spring.api.service.response.ProductResponse;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public List<ProductResponse> getSellingProducts() {

    List<Product> products = productRepository.findAllBySellingStatusIn(
        ProductSellingStatus.forDisplay());
    return products.stream()
        .map(product -> ProductResponse.of(product))
        .collect(Collectors.toList());
  }

}
