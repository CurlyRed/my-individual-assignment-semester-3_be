package Marketplace.domain.Location;

import Marketplace.persistence.entity.LocationEntity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class LocationConverter {


    public Location convert(LocationEntity location){
        return Location.builder()
                .id(location.getId())
                .country(location.getCountry())
                .city(location.getCity())
                .address(location.getAddress())
                .build();
    }

    public LocationEntity convertToEntity(Location location){
        return LocationEntity.builder()
                .id(location.getId())
                .country(location.getCountry())
                .city(location.getCity())
                .address(location.getAddress())
                .build();
    }
}
