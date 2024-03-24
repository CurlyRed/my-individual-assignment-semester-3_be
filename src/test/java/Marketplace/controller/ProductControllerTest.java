package Marketplace.controller;

import Marketplace.business.ProductService;
import Marketplace.domain.Category.Category;
import Marketplace.domain.Product.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
    }

    @Test
    void createProduct_ValidRequest_ReturnsCreatedResponse() {
        // Given
        CreateProductRequest request = CreateProductRequest.builder()
                .productName("Test Product")
                .productDescription("Test Description")
                .categoryId(1L)
                .categoryAttributes(Collections.emptyMap())
                .build();
        CreateProductResponse response = CreateProductResponse.builder()
                .productId(1L)
                .build();
        when(productService.createProduct(request)).thenReturn(response);

        // When
        ResponseEntity<CreateProductResponse> responseEntity = productController.createProduct(request);

        // Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getProduct_ExistingProductId_ReturnsProduct() {
        // Given
        long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .name("Test Product")
                .description("Test Description")
                .category(new Category())
                .attributeValues(Collections.emptyMap())
                .build();
        when(productService.getProduct(productId)).thenReturn(Optional.of(product));

        // When
        ResponseEntity<Product> responseEntity = productController.getProduct(productId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
    }

    @Test
    void getProduct_NonExistingProductId_ReturnsNotFound() {
        // Given
        long productId = 1L;
        when(productService.getProduct(productId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Product> responseEntity = productController.getProduct(productId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertFalse(responseEntity.hasBody());
    }

    @Test
    void updateProduct_ValidRequest_ReturnsNoContent() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = UpdateProductRequest.builder()
                .id(productId)
                .productName("Updated Product")
                .productDescription("Updated Description")
                .categoryAttributes(Collections.emptyMap())
                .build();

        // When
        ResponseEntity<Void> responseEntity = productController.updateProduct(productId, request);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void deleteProduct_ExistingProductId_ReturnsNoContent() {
        // Given
        int productId = 1;

        // When
        ResponseEntity<Void> responseEntity = productController.deleteProduct(productId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void getProducts_ReturnsListOfProducts() {
        // Given
        List<Product> products = Collections.singletonList(
                Product.builder()
                        .id(1L)
                        .name("Test Product")
                        .description("Test Description")
                        .category(new Category())
                        .attributeValues(Collections.emptyMap())
                        .build()
        );
        when(productService.getProducts()).thenReturn(products);

        // When
        ResponseEntity<List<Product>> responseEntity = productController.getProducts();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(products, responseEntity.getBody());
    }
}

