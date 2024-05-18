package Marketplace.persistence.converter;

import Marketplace.domain.ContactInformation;
import Marketplace.persistence.entity.ContactInformationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactInformationConverterTest {

    private ContactInformationConverter contactInformationConverter;

    @BeforeEach
    public void setUp() {
        contactInformationConverter = new ContactInformationConverter();
    }

    @Test
    public void testToDomain_withValidContactInformationEntity_shouldReturnContactInformation() {
        // Given
        ContactInformationEntity contactInformationEntity = ContactInformationEntity.builder()
                .id(1L)
                .contact_person("John Doe")
                .email("john.doe@example.com")
                .phone_number("123-456-7890")
                .build();

        // When
        ContactInformation contactInformation = contactInformationConverter.toDomain(contactInformationEntity);

        // Then
        assertNotNull(contactInformation);
        assertEquals(contactInformationEntity.getId(), contactInformation.getId());
        assertEquals(contactInformationEntity.getContact_person(), contactInformation.getContact_person());
        assertEquals(contactInformationEntity.getEmail(), contactInformation.getEmail());
        assertEquals(contactInformationEntity.getPhone_number(), contactInformation.getPhone_number());
    }

    @Test
    public void testToDomain_withNullContactInformationEntity_shouldReturnNull() {
        // Given
        ContactInformationEntity contactInformationEntity = null;

        // When
        ContactInformation contactInformation = contactInformationConverter.toDomain(contactInformationEntity);

        // Then
        assertNull(contactInformation);
    }

    @Test
    public void testToEntity_withValidContactInformation_shouldReturnContactInformationEntity() {
        // Given
        ContactInformation contactInformation = ContactInformation.builder()
                .id(1L)
                .contact_person("Jane Doe")
                .email("jane.doe@example.com")
                .phone_number("098-765-4321")
                .build();

        // When
        ContactInformationEntity contactInformationEntity = contactInformationConverter.toEntity(contactInformation);

        // Then
        assertNotNull(contactInformationEntity);
        assertEquals(contactInformation.getId(), contactInformationEntity.getId());
        assertEquals(contactInformation.getContact_person(), contactInformationEntity.getContact_person());
        assertEquals(contactInformation.getEmail(), contactInformationEntity.getEmail());
        assertEquals(contactInformation.getPhone_number(), contactInformationEntity.getPhone_number());
    }

    @Test
    public void testToEntity_withNullContactInformation_shouldReturnNull() {
        // Given
        ContactInformation contactInformation = null;

        // When
        ContactInformationEntity contactInformationEntity = contactInformationConverter.toEntity(contactInformation);

        // Then
        assertNull(contactInformationEntity);
    }
}
