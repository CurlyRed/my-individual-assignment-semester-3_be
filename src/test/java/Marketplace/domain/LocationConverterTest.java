package Marketplace.domain;

import static org.junit.jupiter.api.Assertions.*;

import Marketplace.domain.Location.Location;
import Marketplace.domain.Location.LocationConverter;
import Marketplace.persistence.entity.LocationEntity;
import org.junit.jupiter.api.Test;

class LocationConverterTest {

    private final LocationConverter locationConverter = new LocationConverter();

    @Test
    void convert_ValidLocationEntity_ReturnsLocation() {
        // Given
        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .country("Country")
                .city("City")
                .address("Address")
                .build();

        // When
        Location location = locationConverter.convert(locationEntity);

        // Then
        assertNotNull(location);
        assertEquals(1L, location.getId());
        assertEquals("Country", location.getCountry());
        assertEquals("City", location.getCity());
        assertEquals("Address", location.getAddress());
    }

    @Test
    void convertToEntity_ValidLocation_ReturnsLocationEntity() {
        // Given
        Location location = Location.builder()
                .id(1L)
                .country("Country")
                .city("City")
                .address("Address")
                .build();

        // When
        LocationEntity locationEntity = locationConverter.convertToEntity(location);

        // Then
        assertNotNull(locationEntity);
        assertEquals(1L, locationEntity.getId());
        assertEquals("Country", locationEntity.getCountry());
        assertEquals("City", locationEntity.getCity());
        assertEquals("Address", locationEntity.getAddress());
    }
}

