package com.testcode.sample.cafekiosk.spring.api.service.product;

import com.testcode.sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.testcode.sample.cafekiosk.spring.domain.product.Product;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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

  @Transactional
  public ProductResponse createProduct(ProductCreateRequest request) {

    String nextProductNumber = createNextProductNumber();

    Product product = request.toEntity(nextProductNumber);
    Product savedProduct = productRepository.save(product);

    return ProductResponse.of(savedProduct);
  }

  private String createNextProductNumber() {
    String lastestProductNumber = productRepository.findLatestProductNumber();
    if(lastestProductNumber == null) {
      return "001";
    }

    int lastestProductNumberInt = Integer.valueOf(lastestProductNumber) + 1;
    return String.format("%03d", lastestProductNumberInt);
  }

}
