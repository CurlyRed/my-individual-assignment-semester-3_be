package Marketplace.business.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Marketplace.domain.Product.CreateProductRequest;
import Marketplace.domain.Product.CreateProductResponse;
import Marketplace.domain.Product.Product;
import Marketplace.domain.Product.UpdateProductRequest;
import Marketplace.persistence.ProductRepository;
import Marketplace.persistence.entity.ProductEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ValidRequest_ReturnsCreateProductResponse(){
        //Given
        CreateProductRequest request = createProductRequest();
        ProductEntity createdProductEntity = createProductEntity();
        when(productRepository.saveProduct(any(ProductEntity.class))).thenReturn(createdProductEntity);

        //When
        CreateProductResponse response = productService.createProduct(request);

        //Assert
        assertNotNull(response);
        assertEquals(createdProductEntity.getId(), response.getProductId());
    }

    private CreateProductRequest createProductRequest(){
        return CreateProductRequest.builder()
                .productName("product1")
                .productDescription("description1")
                .categoryId(1L)
                .categoryAttributes(createAttributeValues())
                .build();
    }

    private UpdateProductRequest updateProductRequest(){
        return UpdateProductRequest.builder()
                .id(1L)
                .productName("updatedProduct")
                .productDescription("updatedDescription")
                .categoryAttributes(createAttributeValuesUpdated())
                .build();
    }

    private ProductEntity createProductEntity(){
        return ProductEntity.builder()
                .id(1L)
                .name("product1")
                .description("description")
                .categoryId(1L)
                .attributeValues(createAttributeValues())
                .build();
    }

    private Map<String, String> createAttributeValues() {
        Map<String, String> attributeValues = new HashMap<>();
        attributeValues.put("Attribute 1", "Value 1");
        attributeValues.put("Attribute 2", "Value 2");
        return attributeValues;
    }
    private Map<String, String> createAttributeValuesUpdated() {
        Map<String, String> attributeValues = new HashMap<>();
        attributeValues.put("updatedAttribute 1", "updatedValue 1");
        attributeValues.put("updatedAttribute 2", "updatedValue 2");
        return attributeValues;
    }
}
