package Marketplace.persistence.jpaRepository;

import Marketplace.persistence.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByBuyerIdOrSellerId(Long buyerId, Long sellerId);

    @Modifying
    @Query("UPDATE ChatEntity c SET c.deleted = true WHERE c.id = :chatId")
    void disableChat(Long chatId);

    @Modifying
    @Query("UPDATE ChatEntity c SET c.deleted = false WHERE c.id = :chatId")
    void recoverChat(Long chatId);
}
