package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ContactInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInformationRepository extends JpaRepository<ContactInformationEntity, Long> {
}
