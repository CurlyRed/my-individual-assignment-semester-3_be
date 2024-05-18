package Marketplace.business;

import Marketplace.business.dto.product.CreateProductRequest;
import Marketplace.business.dto.product.CreateProductResponse;
import Marketplace.business.dto.product.UpdateProductRequest;
import Marketplace.business.impl.ProductServiceImpl;
import Marketplace.domain.Product;
import Marketplace.domain.ProductAttribute;
import Marketplace.persistence.converter.ProductConverter;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;
import Marketplace.config.security.token.AccessToken;
import Marketplace.business.exception.UnauthorizedDataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductConverter productConverter;
    @Mock
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private ContactInformationRepository contactInformationRepository;
    @Mock
    private AccessToken requestAccessToken;
    @InjectMocks
    private ProductServiceImpl productService;

    private static final String UNAUTHORIZED_EXCEPTION_MESSAGE = "USER_ID_NOT_FROM_LOGGED_IN_USER";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(productService, "unauthorizedExceptionMessage", UNAUTHORIZED_EXCEPTION_MESSAGE);
    }

    @Test
    public void testCreateProduct_withValidRequest_shouldReturnCreateProductResponse() {
        // Given
        CreateProductRequest request = createValidProductRequest();

        CategoryEntity categoryEntity = mock(CategoryEntity.class);
        CityEntity cityEntity = new CityEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        ProductEntity savedProductEntity = new ProductEntity();
        savedProductEntity.setId(1L);
        savedProductEntity.setCategory(categoryEntity);
        savedProductEntity.setCity(cityEntity);
        savedProductEntity.setUser(userEntity);

        List<AttributeEntity> attributeEntities = new ArrayList<>();
        attributeEntities.add(new AttributeEntity());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedProductEntity);
        when(categoryEntity.getAttributes()).thenReturn(attributeEntities);

        // When
        CreateProductResponse response = productService.createProduct(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals(categoryEntity.getId(), response.getCategoryId());

        // Verify
        verify(categoryRepository, times(1)).findById(request.getCategoryId());
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verify(requestAccessToken, times(3)).getUserId();
        verify(productRepository, times(2)).save(any(ProductEntity.class));
        verify(productAttributeRepository, times(1)).saveAll(anyList());
        verify(contactInformationRepository, times(1)).save(any(ContactInformationEntity.class));
    }

    @Test
    public void testCreateProduct_withNullRequest_shouldReturnNull() {
        // When
        CreateProductResponse response = productService.createProduct(null);

        // Then
        assertNull(response);

        // Verify
        verifyNoInteractions(categoryRepository, cityRepository, userRepository, productRepository, productAttributeRepository, contactInformationRepository);
    }

    @Test
    public void testCreateProduct_withNonExistentCategory_shouldThrowException() {
        // Given
        CreateProductRequest request = createValidProductRequest();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));

        // Verify
        verify(categoryRepository, times(1)).findById(request.getCategoryId());
        verifyNoMoreInteractions(categoryRepository);
        verifyNoInteractions(cityRepository, userRepository, productRepository, productAttributeRepository, contactInformationRepository);
    }

    @Test
    public void testCreateProduct_withNonExistentCity_shouldThrowException() {
        // Given
        CreateProductRequest request = createValidProductRequest();

        CategoryEntity categoryEntity = new CategoryEntity();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));

        // Verify
        verify(categoryRepository, times(1)).findById(request.getCategoryId());
        verify(cityRepository, times(1)).findById(request.getCityId());
        verifyNoMoreInteractions(categoryRepository, cityRepository);
        verifyNoInteractions(userRepository, productRepository, productAttributeRepository, contactInformationRepository);
    }

    @Test
    public void testCreateProduct_withNonExistentUser_shouldThrowException() {
        // Given
        CreateProductRequest request = createValidProductRequest();

        CategoryEntity categoryEntity = new CategoryEntity();
        CityEntity cityEntity = new CityEntity();
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));

        // Verify
        verify(categoryRepository, times(1)).findById(request.getCategoryId());
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verifyNoMoreInteractions(categoryRepository, cityRepository, userRepository);
        verifyNoInteractions(productRepository, productAttributeRepository, contactInformationRepository);
    }

    @Test
    public void testCreateProduct_withUnauthorizedUser_shouldThrowException() {
        // Given
        CreateProductRequest request = createValidProductRequest();

        CategoryEntity categoryEntity = new CategoryEntity();
        CityEntity cityEntity = new CityEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(categoryEntity));
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> productService.createProduct(request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(categoryRepository, times(1)).findById(request.getCategoryId());
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verify(requestAccessToken, times(3)).getUserId();
        verifyNoMoreInteractions(categoryRepository, cityRepository, userRepository, requestAccessToken);
        verifyNoInteractions(productRepository, productAttributeRepository, contactInformationRepository);
    }

    @Test
    public void testGetProduct_withExistingProduct_shouldReturnProduct() {
        // Given
        long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        Product product = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productConverter.toDomain(productEntity)).thenReturn(product);

        // When
        Optional<Product> result = productService.getProduct(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product, result.get());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(productConverter, times(1)).toDomain(productEntity);
    }

    @Test
    public void testGetProduct_withNonExistingProduct_shouldReturnEmpty() {
        // Given
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProduct(productId);

        // Then
        assertFalse(result.isPresent());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(productConverter, times(0)).toDomain(any(ProductEntity.class));
    }

    @Test
    public void testUpdateProduct_withValidRequest_shouldUpdateProduct() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = createValidUpdateProductRequest();

        ProductEntity existingProduct = new ProductEntity();
        CityEntity cityEntity = new CityEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        ContactInformationEntity contactInformation = new ContactInformationEntity();

        existingProduct.setContact_information(contactInformation);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(cityRepository.findById(request.getCityId())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // When
        productService.updateProduct(productId, request);

        // Then
        assertEquals(request.getProductName(), existingProduct.getName());
        assertEquals(request.getProductDescription(), existingProduct.getDescription());
        assertEquals(request.getProductPrice(), existingProduct.getPrice());
        assertEquals(cityEntity, existingProduct.getCity());
        assertEquals(request.getContact_person(), contactInformation.getContact_person());
        assertEquals(request.getEmail(), contactInformation.getEmail());
        assertEquals(request.getPhone_number(), contactInformation.getPhone_number());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testUpdateProduct_withNullRequest_shouldThrowException() {
        // Given
        long productId = 1L;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, null));
        assertEquals("Update request cannot be null", exception.getMessage());

        // Verify
        verifyNoInteractions(productRepository, cityRepository, userRepository);
    }

    @Test
    public void testUpdateProduct_withNonExistentProduct_shouldThrowException() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = createValidUpdateProductRequest();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, request));
        assertEquals("Product not found", exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(cityRepository, userRepository);
    }

    @Test
    public void testUpdateProduct_withNonExistentCity_shouldThrowException() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = createValidUpdateProductRequest();

        ProductEntity existingProduct = new ProductEntity();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(cityRepository.findById(request.getCityId())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, request));
        assertEquals("City not found", exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(request.getCityId());
        verifyNoMoreInteractions(productRepository, cityRepository);
        verifyNoInteractions(userRepository);
    }

    @Test
    public void testUpdateProduct_withNonExistentUser_shouldThrowException() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = createValidUpdateProductRequest();

        ProductEntity existingProduct = new ProductEntity();
        CityEntity cityEntity = new CityEntity();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(cityRepository.findById(request.getCityId())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(requestAccessToken.getUserId())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, request));
        assertEquals("User not found", exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verifyNoMoreInteractions(productRepository, cityRepository, userRepository);
    }

    @Test
    public void testUpdateProduct_withUnauthorizedUser_shouldThrowException() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = createValidUpdateProductRequest();

        ProductEntity existingProduct = new ProductEntity();
        CityEntity cityEntity = new CityEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(2L);
        existingProduct.setContact_information(new ContactInformationEntity());

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(cityRepository.findById(request.getCityId())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> productService.updateProduct(productId, request));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verify(requestAccessToken, times(3)).getUserId();
        verifyNoMoreInteractions(productRepository, cityRepository, userRepository, requestAccessToken);
    }

    @Test
    public void testUpdateProduct_withNoContactInformation_shouldThrowException() {
        // Given
        long productId = 1L;
        UpdateProductRequest request = createValidUpdateProductRequest();

        ProductEntity existingProduct = new ProductEntity();
        CityEntity cityEntity = new CityEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(cityRepository.findById(request.getCityId())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(requestAccessToken.getUserId())).thenReturn(Optional.of(userEntity));
        when(requestAccessToken.getUserId()).thenReturn(1L);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(productId, request));

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(request.getCityId());
        verify(userRepository, times(1)).findById(requestAccessToken.getUserId());
        verify(requestAccessToken, times(3)).getUserId();
        verifyNoMoreInteractions(productRepository, cityRepository, userRepository, requestAccessToken);
    }

    @Test
    public void testDeleteProduct_withExistingProduct_shouldReturnTrue() {
        // Given
        long productId = 1L;

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertTrue(result);

        // Verify
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_withNonExistentProduct_shouldReturnFalse() {
        // Given
        long productId = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(productRepository).deleteById(productId);

        // When
        boolean result = productService.deleteProduct(productId);

        // Then
        assertFalse(result);

        // Verify
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testGetProducts_withExistingProducts_shouldReturnProductList() {
        // Given
        ProductEntity productEntity1 = new ProductEntity();
        ProductEntity productEntity2 = new ProductEntity();
        Product product1 = new Product();
        Product product2 = new Product();

        List<ProductEntity> productEntities = Arrays.asList(productEntity1, productEntity2);
        when(productRepository.findAll()).thenReturn(productEntities);
        when(productConverter.toDomain(productEntity1)).thenReturn(product1);
        when(productConverter.toDomain(productEntity2)).thenReturn(product2);

        // When
        List<Product> result = productService.getProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0));
        assertEquals(product2, result.get(1));

        // Verify
        verify(productRepository, times(1)).findAll();
        verify(productConverter, times(2)).toDomain(productEntity1);
        verify(productConverter, times(2)).toDomain(productEntity2);
    }

    @Test
    public void testGetProducts_withNoProducts_shouldReturnEmptyList() {
        // Given
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productService.getProducts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(productRepository, times(1)).findAll();
        verify(productConverter, times(0)).toDomain(any(ProductEntity.class));
    }

    @Test
    public void testGetProductsForUser_withValidUser_shouldReturnProductList() {
        // Given
        long userId = 1L;
        ProductEntity productEntity1 = new ProductEntity();
        ProductEntity productEntity2 = new ProductEntity();
        Product product1 = new Product();
        Product product2 = new Product();

        List<ProductEntity> productEntities = Arrays.asList(productEntity1, productEntity2);
        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(productRepository.findByUserId(userId)).thenReturn(productEntities);
        when(productConverter.toDomain(productEntity1)).thenReturn(product1);
        when(productConverter.toDomain(productEntity2)).thenReturn(product2);

        // When
        List<Product> result = productService.getProductsForUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0));
        assertEquals(product2, result.get(1));

        // Verify
        verify(requestAccessToken, times(1)).getUserId();
        verify(productRepository, times(1)).findByUserId(userId);
        verify(productConverter, times(2)).toDomain(productEntity1);
        verify(productConverter, times(2)).toDomain(productEntity2);
    }

    @Test
    public void testGetProductsForUser_withUnauthorizedUser_shouldThrowException() {
        // Given
        long userId = 1L;
        long unauthorizedUserId = 2L;

        when(requestAccessToken.getUserId()).thenReturn(unauthorizedUserId);

        // When & Then
        UnauthorizedDataAccessException exception = assertThrows(UnauthorizedDataAccessException.class, () -> productService.getProductsForUser(userId));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        // Verify
        verify(requestAccessToken, times(1)).getUserId();
        verifyNoInteractions(productRepository, productConverter);
    }

    @Test
    public void testGetProductsForUser_withNoProducts_shouldReturnEmptyList() {
        // Given
        long userId = 1L;

        when(requestAccessToken.getUserId()).thenReturn(userId);
        when(productRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productService.getProductsForUser(userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(requestAccessToken, times(1)).getUserId();
        verify(productRepository, times(1)).findByUserId(userId);
        verify(productConverter, times(0)).toDomain(any(ProductEntity.class));
    }

    @Test
    public void testGetProductsForCategory_withValidCategory_shouldReturnProductList() {
        // Given
        long categoryId = 1L;
        ProductEntity productEntity1 = new ProductEntity();
        ProductEntity productEntity2 = new ProductEntity();
        Product product1 = new Product();
        Product product2 = new Product();

        List<ProductEntity> productEntities = Arrays.asList(productEntity1, productEntity2);
        when(productRepository.findByCategoryId(categoryId)).thenReturn(productEntities);
        when(productConverter.toDomain(productEntity1)).thenReturn(product1);
        when(productConverter.toDomain(productEntity2)).thenReturn(product2);

        // When
        List<Product> result = productService.getProductsForCategory(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1, result.get(0));
        assertEquals(product2, result.get(1));

        // Verify
        verify(productRepository, times(2)).findByCategoryId(categoryId);
        verify(productConverter, times(2)).toDomain(productEntity1);
        verify(productConverter, times(2)).toDomain(productEntity2);
    }

    @Test
    public void testGetProductsForCategory_withNoProductsInCategory_shouldThrowException() {
        // Given
        long categoryId = 1L;

        when(productRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductsForCategory(categoryId));
        assertEquals("Category does not have products yet", exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findByCategoryId(categoryId);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(productConverter);
    }

    private CreateProductRequest createValidProductRequest() {
        return CreateProductRequest.builder()
                .productName("Test Product")
                .productDescription("Test Description")
                .productPrice(100.0)
                .dateOfPost(new java.util.Date())
                .categoryId(1L)
                .cityId(1L)
                .attributes(Collections.singletonList(
                        ProductAttribute.builder()
                                .value("Value")
                                .build()
                ))
                .contact_person("John Doe")
                .email("john.doe@example.com")
                .phone_number("123-456-7890")
                .build();
    }

    private UpdateProductRequest createValidUpdateProductRequest() {
        return UpdateProductRequest.builder()
                .productName("Updated Product")
                .productDescription("Updated Description")
                .productPrice(150.0)
                .cityId(1L)
                .contact_person("Updated Contact Person")
                .email("updated.email@example.com")
                .phone_number("987-654-3210")
                .build();
    }
}