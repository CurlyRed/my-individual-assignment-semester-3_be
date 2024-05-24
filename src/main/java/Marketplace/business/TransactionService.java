package Marketplace.business;

import Marketplace.domain.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactions();
    List<Transaction> getTransactionsForUser(long userId);
}
