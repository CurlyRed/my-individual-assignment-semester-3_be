package Marketplace.persistence.converter;

import Marketplace.domain.District;
import Marketplace.persistence.entity.DistrictEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class DistrictConverter {
    public final CityConverter cityConverter;

    public District toDomain(DistrictEntity districtEntity){
        if(districtEntity == null){
            return null;
        }

        return District.builder()
                .id(districtEntity.getId())
                .name(districtEntity.getName())
                .cities(districtEntity.getCities().stream()
                        .map(cityConverter::toDomain)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
    }
}
