package Marketplace.business;

import Marketplace.business.dto.product.GetLocationForProductResponse;
import Marketplace.business.impl.DistrictServiceImpl;
import Marketplace.domain.District;
import Marketplace.persistence.converter.DistrictConverter;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.DistrictEntity;
import Marketplace.persistence.entity.ProductEntity;
import Marketplace.persistence.jpaRepository.CityRepository;
import Marketplace.persistence.jpaRepository.DistrictRepository;
import Marketplace.persistence.jpaRepository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
class DistrictServiceImplTest {
    @Mock
    private DistrictRepository districtRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CityRepository cityRepository;
    @Mock
    private DistrictConverter districtConverter;
    @InjectMocks
    private DistrictServiceImpl districtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetDistricts_withExistingDistricts_shouldReturnDistrictList() {
        // Given
        DistrictEntity districtEntity1 = new DistrictEntity();
        DistrictEntity districtEntity2 = new DistrictEntity();
        District district1 = new District();
        District district2 = new District();

        List<DistrictEntity> districtEntities = Arrays.asList(districtEntity1, districtEntity2);
        when(districtRepository.findAll()).thenReturn(districtEntities);
        when(districtConverter.toDomain(districtEntity1)).thenReturn(district1);
        when(districtConverter.toDomain(districtEntity2)).thenReturn(district2);

        // When
        List<District> result = districtService.getDistricts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(district1, result.get(0));
        assertEquals(district2, result.get(1));

        // Verify
        verify(districtRepository, times(1)).findAll();
        verify(districtConverter, times(2)).toDomain(districtEntity1);
        verify(districtConverter, times(2)).toDomain(districtEntity2);
    }

    @Test
    void testGetDistricts_withNoDistricts_shouldReturnEmptyList() {
        // Given
        when(districtRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<District> result = districtService.getDistricts();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // Verify
        verify(districtRepository, times(1)).findAll();
        verify(districtConverter, times(0)).toDomain(any(DistrictEntity.class));
    }

    @Test
    void testGetLocationForProduct_withExistingProduct_shouldReturnLocation() {
        // Given
        long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        CityEntity cityEntity = new CityEntity();
        DistrictEntity districtEntity = new DistrictEntity();
        productEntity.setCity(cityEntity);

        cityEntity.setId(1L);
        cityEntity.setDistrict(districtEntity);
        districtEntity.setId(1L);
        districtEntity.setName("Test District");
        cityEntity.setName("Test City");

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(cityRepository.findById(cityEntity.getId())).thenReturn(Optional.of(cityEntity));
        when(districtRepository.findById(districtEntity.getId())).thenReturn(Optional.of(districtEntity));

        // When
        GetLocationForProductResponse response = districtService.getLocationForProduct(productId);

        // Then
        assertNotNull(response);
        assertEquals("Test District", response.getDistrictName());
        assertEquals("Test City", response.getCityName());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(cityEntity.getId());
        verify(districtRepository, times(1)).findById(districtEntity.getId());
    }

    @Test
    void testGetLocationForProduct_withNonExistentProduct_shouldThrowException() {
        // Given
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> districtService.getLocationForProduct(productId));
        assertEquals("Product not found", exception.getMessage());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verifyNoInteractions(cityRepository, districtRepository);
    }

    @Test
    void testGetLocationForProduct_withNonExistentCity_shouldReturnNullLocation() {
        // Given
        long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        CityEntity cityEntity = new CityEntity();
        productEntity.setCity(cityEntity);

        cityEntity.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(cityRepository.findById(cityEntity.getId())).thenReturn(Optional.empty());

        // When
        GetLocationForProductResponse response = districtService.getLocationForProduct(productId);

        // Then
        assertNotNull(response);
        assertNull(response.getCityName());
        assertNull(response.getDistrictName());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(cityEntity.getId());
        verifyNoInteractions(districtRepository);
    }

    @Test
    void testGetLocationForProduct_withNonExistentDistrict_shouldReturnCityOnly() {
        // Given
        long productId = 1L;
        ProductEntity productEntity = new ProductEntity();
        CityEntity cityEntity = new CityEntity();
        DistrictEntity districtEntity = new DistrictEntity();
        productEntity.setCity(cityEntity);

        cityEntity.setId(1L);
        cityEntity.setName("Test City");
        cityEntity.setDistrict(districtEntity);
        districtEntity.setId(1L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(cityRepository.findById(cityEntity.getId())).thenReturn(Optional.of(cityEntity));
        when(districtRepository.findById(districtEntity.getId())).thenReturn(Optional.empty());

        // When
        GetLocationForProductResponse response = districtService.getLocationForProduct(productId);

        // Then
        assertNotNull(response);
        assertEquals("Test City", response.getCityName());
        assertNull(response.getDistrictName());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(cityRepository, times(1)).findById(cityEntity.getId());
        verify(districtRepository, times(1)).findById(districtEntity.getId());
    }
}
