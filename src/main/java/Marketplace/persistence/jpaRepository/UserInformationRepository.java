package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.UserInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserInformationRepository extends JpaRepository<UserInformationEntity, Long> {
    @Query("SELECT u.age, COUNT(u) FROM UserInformationEntity u GROUP BY u.age")
    List<Object[]> countUsersByAge();

    @Query("SELECT u.gender, COUNT(u) FROM UserInformationEntity u GROUP BY u.gender")
    List<Object[]> countUsersByGender();

    @Query("SELECT d.name, c.name, COUNT(u) " +
            "FROM UserEntity u " +
            "JOIN u.userInformation ui " +
            "JOIN ui.city c " +
            "JOIN c.district d " +
            "GROUP BY d.name, c.name")
    List<Object[]> countUsersByLocation();
}
