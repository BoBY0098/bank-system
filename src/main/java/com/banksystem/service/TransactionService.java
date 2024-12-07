package com.banksystem.service;


import com.banksystem.entity.BankAccountEntity;
import com.banksystem.entity.TransactionEntity;
import com.banksystem.entity.TransactionType;
import com.banksystem.logger.observer.TransactionObserver;
import com.banksystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final List<TransactionObserver> transactionObservers;
    private final TransactionRepository transactionRepository;
    private final ReentrantLock lock = new ReentrantLock();
    private final ExecutorService executorService;

    public void deposit(BankAccountEntity bankAccountEntity, BigDecimal amount) {
        negativeAmountCheck(amount);
        executorService.submit( () -> {
            lock.lock();
            try {
                TransactionEntity transaction = TransactionEntity.builder()
                        .type(TransactionType.DEPOSIT)
                        .amount(amount)
                        .account(bankAccountEntity)
                        .build();
                transactionRepository.save(transaction);
                notifyObservers(transaction);
            } finally {
                lock.unlock();
            }
        });
    }

    public void withdraw(BankAccountEntity bankAccountEntity, BigDecimal amount) {
        negativeAmountCheck(amount);
        balanceValidation(bankAccountEntity,amount);
        executorService.submit(() -> {
            lock.lock();
            try {
                TransactionEntity transaction = TransactionEntity.builder()
                        .type(TransactionType.WITHDRAW)
                        .amount(amount)
                        .account(bankAccountEntity)
                        .build();
                transactionRepository.save(transaction);
                notifyObservers(transaction);
            } finally {
                lock.unlock();
            }
        });
    }

    public void transfer(BankAccountEntity fromAccount,BankAccountEntity toAccount, BigDecimal amount) {
        negativeAmountCheck(amount);
        balanceValidation(fromAccount,amount);
        executorService.submit(() -> {
            lock.lock();
            try {
                TransactionEntity transaction = TransactionEntity.builder()
                        .type(TransactionType.TRANSFER)
                        .amount(amount)
                        .account(fromAccount)
                        .targetAccount(toAccount)
                        .build();
                transactionRepository.save(transaction);
                notifyObservers(transaction);
            } finally {
                lock.unlock();
            }
        });
    }

    public BigDecimal getBalanceFromTransactions(Long accountId) {
        //since this is read only, we do not need to lock/unlock
        return transactionRepository.getBalanceFromTransactions(accountId);
    }

    private void notifyObservers(TransactionEntity transaction){
        if (transactionObservers == null) {
            return;
        }
        for (TransactionObserver transactionObserver : transactionObservers) {
            transactionObserver.onTransaction(transaction);
        }
    }

    private void negativeAmountCheck(BigDecimal initialBalance) {
        Objects.requireNonNull(initialBalance);
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    private void balanceValidation(BankAccountEntity bankAccountEntity, BigDecimal amount) {
        if (bankAccountEntity.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0){
            //cannot withdraw more than balance
            throw new IllegalStateException("Cannot withdraw more than current balance");
        }
    }
}
