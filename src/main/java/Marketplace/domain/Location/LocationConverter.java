package Marketplace.domain.Location;

import Marketplace.domain.Location.Location;
import Marketplace.persistence.entity.LocationEntity;

public final class LocationConverter {

    private LocationConverter(){

    }

    public static Location convert(LocationEntity location){
        return Location.builder()
                .id(location.getId())
                .country(location.getCountry())
                .city(location.getCity())
                .address(location.getAddress())
                .build();
    }

    public static LocationEntity convertToEntity(Location location){
        return LocationEntity.builder()
                .id(location.getId())
                .country(location.getCountry())
                .city(location.getCity())
                .address(location.getAddress())
                .build();
    }
}
