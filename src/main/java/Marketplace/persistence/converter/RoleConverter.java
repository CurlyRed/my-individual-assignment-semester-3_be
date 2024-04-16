package Marketplace.persistence.converter;

import Marketplace.domain.Role;
import Marketplace.persistence.entity.RoleEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleConverter {
    public Role toDomain(RoleEntity roleEntity){
        if(roleEntity == null){
            return null;
        }

        return Role.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .build();
    }
}
