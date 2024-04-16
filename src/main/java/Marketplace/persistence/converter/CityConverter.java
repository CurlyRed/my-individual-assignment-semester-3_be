package Marketplace.persistence.converter;

import Marketplace.domain.City;
import Marketplace.persistence.entity.CityEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CityConverter {
    private final ProductConverter productConverter;

    public City toDomain(CityEntity cityEntity){
        if(cityEntity == null){
            return null;
        }

        return City.builder()
                .id(cityEntity.getId())
                .name(cityEntity.getName())
                .products(cityEntity.getProducts().stream()
                        .map(productConverter::toDomain)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }
}
