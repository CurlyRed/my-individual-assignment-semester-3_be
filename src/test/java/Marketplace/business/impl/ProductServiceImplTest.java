package Marketplace.business.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Marketplace.domain.Product.*;
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
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    void createProduct_ValidRequest_ReturnsCreateProductResponse() {
        // Given
        CreateProductRequest request = createProductRequest();
        ProductEntity createdProductEntity = createProductEntity(1L);
        when(productRepository.saveProduct(any(ProductEntity.class))).thenReturn(createdProductEntity);

        // When
        CreateProductResponse response = productService.createProduct(request);

        // Then
        assertNotNull(response);
        assertEquals(createdProductEntity.getId(), response.getProductId());
        assertEquals(createdProductEntity.getCategoryId(), response.getCategoryId());
    }

    @Test
    void createProduct_NullRequest_ReturnsNull() {
        // Given
        CreateProductRequest request = null;

        // When
        CreateProductResponse response = productService.createProduct(request);

        // Then
        assertNull(response);
    }

    @Test //Fail
    void getProduct_ExistingId_ReturnsProduct(){
        // Given
        long productId = 1L;
        ProductEntity productEntity = createProductEntity(productId);
        when(productRepository.getProduct(productId)).thenReturn(Optional.of(productEntity));
        when(productConverter.convertToDomain(productEntity)).thenReturn(new Product());

        // When
        Optional<Product> productOptional = productService.getProduct(productId);

        // Then
        assertTrue(productOptional.isPresent());
    }

    @Test
    void getProduct_NonExistingId_ReturnsEmptyOptional(){
        // Given
        long nonExistingProductId = 100L;
        when(productRepository.getProduct(nonExistingProductId)).thenReturn(Optional.empty());

        // When
        Optional<Product> productOptional = productService.getProduct(nonExistingProductId);

        // Then
        assertTrue(productOptional.isEmpty());
    }

    @Test
    void updateProduct_ExistingProduct_ReturnsTrue(){
        // Given
        UpdateProductRequest request = updateProductRequest();
        ProductEntity existingProductEntity = createProductEntity(1L);
        when(productRepository.getProduct(request.getId())).thenReturn(Optional.of(existingProductEntity));

        // When
        boolean result = productService.updateProduct(request);

        // Then
        assertTrue(result);
    }

    @Test
    void updateProduct_NonExistingProduct_ReturnsFalse(){
        // Given
        UpdateProductRequest request = updateProductRequest();
        long nonExistingProductId = 100L;
        when(productRepository.getProduct(nonExistingProductId)).thenReturn(Optional.empty());

        // When
        boolean result = productService.updateProduct(request);

        // Then
        assertFalse(result);
    }

    @Test
    void deleteByid_ExistingProductId_ReturnsTrue(){
        // Given
        long productId = 1L;
        when(productRepository.deleteProduct(productId)).thenReturn(true);

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertTrue(result);
    }

    @Test
    void deleteById_NonExistingProductId_ReturnsFalse(){
        // Given
        long productId = 100L;
        when(productRepository.deleteProduct(productId)).thenReturn(false);

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertFalse(result);
    }

    @Test
    void getProducts_ReturnsListOfProducts() {
        // Given
        List<ProductEntity> productEntities = createProductEntityList();
        List<Product> expectedProducts = productEntities.stream()
                .map(productConverter::convertToDomain)
                .collect(Collectors.toList());
        when(productRepository.getProducts()).thenReturn(productEntities);

        // When
        List<Product> actualProducts = productService.getProducts();

        // Then
        assertEquals(expectedProducts, actualProducts);
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

    private ProductEntity createProductEntity(long id) {
        return ProductEntity.builder()
                .id(id)
                .name("product" + id)
                .description("description" + id)
                .categoryId(id)
                .attributeValues(createAttributeValues())
                .build();
    }

    private List<ProductEntity> createProductEntityList() {
        List<ProductEntity> entities = new ArrayList<>();
        entities.add(createProductEntity(1L));
        entities.add(createProductEntity(2L));
        return entities;
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
