package com.testcode.sample.cafekiosk.spring.domain.product;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findAllBySellingStatusIn(List<ProductSellingStatus> sellingStatuses);

  List<Product> findAllByProductNumberIn(List<String> productNumbers);

  @Query(value = "select p.product_number from Product p order by p.id desc limit 1", nativeQuery = true)
  String findLatestProductNumber();

  Product findByProductNumber(String latestProductNumber);
}
