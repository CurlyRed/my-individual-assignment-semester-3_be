package Marketplace.controller;

import Marketplace.business.TransactionService;
import Marketplace.domain.Transaction;
import jakarta.annotation.security.RolesAllowed;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @RolesAllowed("ADMIN")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        transactionService.getTransactions();
        return ResponseEntity.ok().build();
    }

    @RolesAllowed("USER")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<Transaction>> getTransactionsForUser(@PathVariable Long id) {
        List<Transaction> userTransactions = transactionService.getTransactionsForUser(id);
        return ResponseEntity.ok(userTransactions);
    }
}
