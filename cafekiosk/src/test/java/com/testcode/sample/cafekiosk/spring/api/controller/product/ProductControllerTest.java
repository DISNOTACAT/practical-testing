package com.testcode.sample.cafekiosk.spring.api.controller.product;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testcode.sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.testcode.sample.cafekiosk.spring.api.service.product.ProductResponse;
import com.testcode.sample.cafekiosk.spring.api.service.product.ProductService;
import com.testcode.sample.cafekiosk.spring.domain.order.OrderRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductRepository;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.testcode.sample.cafekiosk.spring.domain.product.ProductType;
import com.testcode.sample.cafekiosk.spring.domain.stock.StockRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper; //json 직렬화 역직렬화

  @MockitoBean
  private ProductService productService;

  private ProductRepository productRepository;
  private OrderRepository orderRepository;
  private StockRepository stockRepository;


  @DisplayName("신규 상품을 등록한다.")
  @Test
  void createProduct() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.SELLING)
            .name("아메리카노")
            .price(4000)
            .build();

    // when // then
    mockMvc.perform(
          post("/api/v1/products/new")
          .content(objectMapper.writeValueAsString(request))
          .contentType(MediaType.APPLICATION_JSON)
          )
          .andDo(print())
          .andExpect(status().isOk());
  }

  @DisplayName("신규 상품을 등록할 때 상품타입은 필수값이다.")
  @Test
  void createProductWithoutType() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
    ;
  }

  @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
  @Test
  void createProductWithoutSellingStatus() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .name("아메리카노")
        .price(4000)
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
    ;
  }

  @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
  @Test
  void createProductWithoutName() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .price(4000)
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
        .andExpect(jsonPath("$.data").isEmpty());
    ;
  }

  @DisplayName("신규 상품을 등록할 때 상품 가격은 양수값이다.")
  @Test
  void createProductWithNegativePrice() throws Exception {
    // given
    ProductCreateRequest request = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(0)
        .build();

    // when // then
    mockMvc.perform(
            post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합니다."))
        .andExpect(jsonPath("$.data").isEmpty());
    ;
  }

  @DisplayName("판매상품을 조회한다.")
  @Test
  void getSellingProducts() throws Exception {
    // given
    List<ProductResponse> result = List.of();
    when(productService.getSellingProducts()).thenReturn(result);

    // when // then
    mockMvc.perform(
            get("/api/v1/products/selling")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value("200"))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray()); // 반환 타입만 검증
    ;
  }
}