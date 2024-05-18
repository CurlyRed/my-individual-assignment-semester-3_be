package Marketplace.persistence.converter;

import Marketplace.domain.AppBalance;
import Marketplace.persistence.entity.AppBalanceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AppBalanceConverterTest {

    private AppBalanceConverter appBalanceConverter;

    @BeforeEach
    void setUp() {
        appBalanceConverter = new AppBalanceConverter();
    }

    @Test
    void testToDomain_withValidBalanceEntity_shouldReturnAppBalance() {
        // Given
        AppBalanceEntity balanceEntity = new AppBalanceEntity();
        balanceEntity.setId(1L);
        balanceEntity.setBalance(100.0);
        balanceEntity.setLast_update(new Date());

        // When
        AppBalance appBalance = appBalanceConverter.toDomain(balanceEntity);

        // Then
        assertNotNull(appBalance);
        assertEquals(balanceEntity.getId(), appBalance.getId());
        assertEquals(balanceEntity.getBalance(), appBalance.getBalance());
        assertEquals(balanceEntity.getLast_update(), appBalance.getLast_update());
    }

    @Test
    public void testToDomain_withNullBalanceEntity_shouldReturnNull() {
        // Given
        AppBalanceEntity balanceEntity = null;

        // When
        AppBalance appBalance = appBalanceConverter.toDomain(balanceEntity);

        // Then
        assertNull(appBalance);
    }
}

