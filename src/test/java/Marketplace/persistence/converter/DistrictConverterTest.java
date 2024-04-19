package Marketplace.persistence.converter;

import Marketplace.domain.City;
import Marketplace.domain.District;
import Marketplace.persistence.entity.CityEntity;
import Marketplace.persistence.entity.DistrictEntity;
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

class DistrictConverterTest {

    private DistrictConverter converter;

    @Mock
    private CityConverter cityConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        converter = new DistrictConverter(cityConverter);
    }

    @Test
    void testToDomain_givenNonNullEntity_shouldConvertCorrectly() {
        // Given
        CityEntity cityEntity = new CityEntity();
        cityEntity.setId(1L);
        cityEntity.setName("Test City");

        DistrictEntity districtEntity = new DistrictEntity();
        districtEntity.setId(1L);
        districtEntity.setName("Test District");
        districtEntity.setCities(Arrays.asList(cityEntity));

        when(cityConverter.toDomain(cityEntity)).thenReturn(
                City.builder().id(1L).name("Test City").build());

        // When
        District district = converter.toDomain(districtEntity);

        // Then
        assertNotNull(district);
        assertEquals(districtEntity.getId(), district.getId());
        assertEquals(districtEntity.getName(), district.getName());

        List<City> cities = district.getCities();
        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertEquals(cityEntity.getId(), cities.get(0).getId());
        assertEquals(cityEntity.getName(), cities.get(0).getName());
    }

    @Test
    void testToDomain_givenNullEntity_shouldReturnNull() {
        // Given
        DistrictEntity districtEntity = null;

        // When
        District district = converter.toDomain(districtEntity);

        // Then
        assertNull(district);
    }
}