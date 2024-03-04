package Marketplace.persistence.entity;

import Marketplace.persistence.entity.LocationEntity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocationEntity location;

}
