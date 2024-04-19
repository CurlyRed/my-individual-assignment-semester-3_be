package Marketplace.business;

import Marketplace.business.dto.product.CreateProductRequest;
import Marketplace.business.dto.product.CreateProductResponse;
import Marketplace.business.dto.user.CreateUserResponse;
import Marketplace.business.impl.ProductServiceImpl;
import Marketplace.domain.Product;
import Marketplace.domain.ProductAttribute;
import Marketplace.persistence.converter.ProductConverter;
import Marketplace.persistence.entity.*;
import Marketplace.persistence.jpaRepository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
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
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private ProductConverter productConverter;
    @InjectMocks
    private ProductServiceImpl productService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createProduct_CreatesProduct(){
        // Given
        List<AttributeEntity> attributesCategory = new ArrayList<>();
        AttributeEntity attributeCategory = AttributeEntity.builder()
                .id(1L)
                .name("xD")
                .build();
        attributesCategory.add(attributeCategory);
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(1L)
                .name("testCategory")
                .attributes(attributesCategory)
                .build();
        DistrictEntity districtEntity = DistrictEntity.builder()
                .id(1L)
                .name("testDistrict")
                .build();
        CityEntity cityEntity = CityEntity.builder()
                .id(1L)
                .name("testCity")
                .district(districtEntity)
                .build();
        RoleEntity roleEntity = RoleEntity.builder()
                .id(1L)
                .name("testRole")
                .build();
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .email("test@email")
                .password("123")
                .firstName("Serhii")
                .lastName("Sokyrko")
                .role(roleEntity)
                .build();
        List<ProductAttributeEntity> productAttributes = new ArrayList<>();
        ProductAttributeEntity productAttributeEntity = ProductAttributeEntity.builder()
                .value("1")
                .build();
        List<ProductAttribute> productAttributesDomain = new ArrayList<>();
        ProductAttribute productAttribute = ProductAttribute.builder()
                .value("1")
                .build();
        productAttributes.add(productAttributeEntity);
        CreateProductRequest request = CreateProductRequest.builder()
                .productName("test")
                .productDescription("testDescription")
                .cityId(cityEntity.getId())
                .userId(userEntity.getId())
                .categoryId(categoryEntity.getId())
                .attributes(productAttributesDomain)
                .build();
        ProductEntity savedProductEntity = ProductEntity.builder()
                .id(1L)
                .name(request.getProductName())
                .description(request.getProductDescription())
                .category(categoryEntity)
                .city(cityEntity)
                .user(userEntity)
                .product_attributes(productAttributes)
                .build();
        // Mock
        when(categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
        when(cityRepository.findById(cityEntity.getId())).thenReturn(Optional.of(cityEntity));
        when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(savedProductEntity);

        // When
        CreateProductResponse response = productService.createProduct(request);

        // Verify
        verify(categoryRepository, times(1)).findById(categoryEntity.getId());
        verify(cityRepository, times(1)).findById(cityEntity.getId());
        verify(userRepository, times(1)).findById(userEntity.getId());
        verify(productRepository, times(2)).save(any(ProductEntity.class));

        // Then
        assertNotNull(response);
        assertEquals(savedProductEntity.getId(), response.getProductId());
        assertEquals(categoryEntity.getId(), response.getCategoryId());
    }

    @Test
    void createProduct_NullRequest(){
        // Given
        CreateProductRequest request = null;

        // When
        CreateProductResponse response = productService.createProduct(request);

        // Then
        assertNull(response);
    }

    @Test
    void getProduct_ReturnOptionalEmpty_WhenProductNotFound(){
        // Given
        long productId = 1L;

        // Mock
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        Optional<Product> result = productService.getProduct(productId);

        // Verify
        verify(productRepository, times(1)).findById(productId);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void getProduct_ReturnsProduct_WhenProductFound(){
        // Given
        long productId = 1L;
        ProductEntity productEntity = ProductEntity.builder()
                .id(productId)
                .build();

        Product product = Product.builder()
                .id(productId)
                .build();

        // Mock
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productConverter.toDomain(productEntity)).thenReturn(product);

        // When
        Optional<Product> result = productService.getProduct(productId);

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(productConverter, times(1)).toDomain(productEntity);

        // Then
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void deleteProduct_DeleteProduct_WhenProductNotFound(){
        // Given
        long productId = 1L;

        // Mock
        doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(productId);

        // When
        boolean isDeleted = productService.deleteProduct(productId);

        // Verify
        verify(productRepository, times(1)).deleteById(productId);

        // Then
        assertFalse(isDeleted);
    }

    @Test
    void deleteProduct_ReturnsFalse_WhenProductNotFound(){
        // Given
        long productId = 1L;

        // Mock
        doNothing().when(productRepository).deleteById(productId);

        // When
        boolean isDeleted = productService.deleteProduct(productId);

        // Verify
        verify(productRepository, times(1)).deleteById(productId);

        // Then
        assertTrue(isDeleted);
    }

    @Test
    void getProducts_ReturnsListOfConvertedProducts_WhenProductsFound(){
        // Given
        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(ProductEntity.builder().id(1L).name("p1").build());
        productEntities.add(ProductEntity.builder().id(2L).name("p2").build());

        // Mock
        when(productRepository.findAll()).thenReturn(productEntities);
        when(productConverter.toDomain(any(ProductEntity.class)))
                .thenAnswer(invocation -> {
                    ProductEntity entity = invocation.getArgument(0);
                    return Product.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .build();
                });

        // When
        List<Product> products = productService.getProducts();

        // Verify
        verify(productRepository, times(1)).findAll();
        verify(productConverter, times(productEntities.size())).toDomain(any(ProductEntity.class));

        // Then
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void getProducts_ReturnsEmptyList_WhenProductsNotFound(){
        // Mock
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        List<Product> products = productService.getProducts();

        // Verify
        verify(productRepository, times(1)).findAll();

        // Then
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }
}
