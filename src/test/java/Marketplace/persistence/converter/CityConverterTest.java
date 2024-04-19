package Marketplace.persistence.converter;

import Marketplace.domain.City;
import Marketplace.domain.Product;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class CityConverterTest {

    private CityConverter converter;

    @Mock
    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new CityConverter(productConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("Test Product");

        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(1L);
        cityEntity.setName("Test City");
        cityEntity.setProducts(Arrays.asList(productEntity));

        when(productConverter.toDomain(productEntity)).thenReturn(
                Product.builder().id(1L).name("Test Product").build());

        // When
        City city = converter.toDomain(cityEntity);

        // Then
        assertNotNull(city);
        assertEquals(cityEntity.getId(), city.getId());
        assertEquals(cityEntity.getName(), city.getName());

        List<Product> products = city.getProducts();
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productEntity.getId(), products.get(0).getId());
        assertEquals(productEntity.getName(), products.get(0).getName());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        CityEntity cityEntity = null;

        // When
        City city = converter.toDomain(cityEntity);

        // Then
        assertNull(city);
    }
}


