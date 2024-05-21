package Marketplace.controller;

import Marketplace.business.WalletService;
import Marketplace.business.dto.walletOperations.PurchasePromotionRequest;
import Marketplace.business.dto.walletOperations.TopUpRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {
    private WalletService walletService;

    @RolesAllowed("USER")
    @PostMapping("/topup")
    public ResponseEntity<?> topUpWallet(@RequestBody @Valid TopUpRequest request) {
        walletService.topUp(request);
        return ResponseEntity.ok(String.format("Top-up successful. Balance topped up by: %.2f", request.getAmount()));
    }

    @RolesAllowed("USER")
    @PostMapping("/purchasepromotion")
    public ResponseEntity<?> purchasePromotion(@RequestBody @Valid PurchasePromotionRequest request) {
        walletService.purchasePromotion(request);
        return ResponseEntity.ok(String.format(
                "Promotion purchase successful for product ID: %d\nAmount paid: %.2f",
                request.getProductId(),
                request.getAmount()
        ));
    }
}
