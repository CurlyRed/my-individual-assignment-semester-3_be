package Marketplace.persistence.converter;

import Marketplace.domain.ContactInformation;
import Marketplace.persistence.entity.ContactInformationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContactInformationConverter {
    public ContactInformation toDomain(ContactInformationEntity contactInformationEntity) {
        if (contactInformationEntity == null) {
            return null;
        }

        return ContactInformation.builder()
                .id(contactInformationEntity.getId())
                .contact_person(contactInformationEntity.getContact_person())
                .email(contactInformationEntity.getEmail())
                .phone_number(contactInformationEntity.getPhone_number())
                .build();
    }

    public ContactInformationEntity toEntity(ContactInformation contactInformation) {
        if (contactInformation == null) {
            return null;
        }

        return ContactInformationEntity.builder()
                .id(contactInformation.getId())
                .contact_person(contactInformation.getContact_person())
                .email(contactInformation.getEmail())
                .phone_number(contactInformation.getPhone_number())
                .build();
    }
}
