package Marketplace.business;

import Marketplace.business.dto.walletOperations.PurchasePromotionRequest;
import Marketplace.business.dto.walletOperations.TopUpRequest;

public interface WalletService {
    void topUp(TopUpRequest request);
    void purchasePromotion(PurchasePromotionRequest request);
}
