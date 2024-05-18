package Marketplace.persistence.converter;

import Marketplace.domain.UserBalance;
import Marketplace.persistence.entity.UserBalanceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserBalanceConverterTest {

    private UserBalanceConverter userBalanceConverter;

    @BeforeEach
    void setUp() {
        userBalanceConverter = new UserBalanceConverter();
    }

    @Test
    void testToDomain_withValidUserBalanceEntity_shouldReturnUserBalance() {
        // Given
        UserBalanceEntity userBalanceEntity = new UserBalanceEntity();
        userBalanceEntity.setId(1L);
        userBalanceEntity.setBalance(100.0);
        userBalanceEntity.setLast_update(new Date());

        // When
        UserBalance userBalance = userBalanceConverter.toDomain(userBalanceEntity);

        // Then
        assertNotNull(userBalance);
        assertEquals(userBalanceEntity.getId(), userBalance.getId());
        assertEquals(userBalanceEntity.getBalance(), userBalance.getBalance());
        assertEquals(userBalanceEntity.getLast_update(), userBalance.getLast_update());
    }

    @Test
    public void testToDomain_withNullUserBalanceEntity_shouldReturnNull() {
        // Given
        UserBalanceEntity userBalanceEntity = null;

        // When
        UserBalance userBalance = userBalanceConverter.toDomain(userBalanceEntity);

        // Then
        assertNull(userBalance);
    }
}

