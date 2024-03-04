package Marketplace.persistence.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationEntity {

    private Long id;
    private String country;
    private String city;
    private String address;

}
