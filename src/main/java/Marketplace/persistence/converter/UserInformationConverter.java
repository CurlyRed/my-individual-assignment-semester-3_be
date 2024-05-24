package Marketplace.persistence.converter;

import Marketplace.domain.UserInformation;
import Marketplace.enums.Gender;
import Marketplace.persistence.entity.UserInformationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserInformationConverter {
    private final CityConverter cityConverter;

    public UserInformation toDomain(UserInformationEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserInformation.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .city(cityConverter.toDomain(entity.getCity()))
                .age(entity.getAge())
                .gender(entity.getGender())
                .build();
    }
}
