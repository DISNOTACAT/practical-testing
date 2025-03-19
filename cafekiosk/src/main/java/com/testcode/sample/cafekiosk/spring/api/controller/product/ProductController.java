package com.testcode.sample.cafekiosk.spring.api.controller.product;

import com.testcode.sample.cafekiosk.spring.api.service.product.ProductService;
import com.testcode.sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping("/api/v1/products/selling")
  public List<ProductResponse> getSellingProducts() {
    return productService.getSellingProducts();
  }

}
