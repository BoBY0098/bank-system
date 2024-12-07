package com.banksystem.service;

import com.banksystem.entity.BankAccountEntity;
import com.banksystem.repository.BankAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionService transactionService;

    public BankAccountEntity createAccount(String accountHolderName, BigDecimal initialBalance) {
        if (initialBalance.compareTo(BigDecimal.ZERO)<0){
            initialBalance = BigDecimal.ZERO;
        }
        BankAccountEntity account = BankAccountEntity.builder()
                .name(accountHolderName)
                .balance(BigDecimal.ZERO)
                .build();
        account = bankAccountRepository.save(account);
        deposit(account.getId(),initialBalance);
        return account;
    }

    public BankAccountEntity findAccount(Long accountNumber) {
        return bankAccountRepository.findById(accountNumber).orElseThrow(EntityNotFoundException::new);
    }

    public BigDecimal getBalance(Long accountNumber) {
        BankAccountEntity bankAccountEntity = bankAccountRepository.findById(accountNumber).orElseThrow(EntityNotFoundException::new);
        return bankAccountEntity.getBalance();
    }

    public void deposit(Long accountNumberDeposit, BigDecimal depositAmount) {
        BankAccountEntity account = findAccount(accountNumberDeposit);
        transactionService.deposit(account,depositAmount);

        account.setBalance(account.getBalance().add(depositAmount));
        bankAccountRepository.save(account);
    }

    public void withdraw(Long accountNumberWithdraw, BigDecimal withdrawAmount) {
        BankAccountEntity account = findAccount(accountNumberWithdraw);
        transactionService.withdraw(account,withdrawAmount);


        account.setBalance(account.getBalance().subtract(withdrawAmount));
        bankAccountRepository.save(account);
    }

    public void transfer(Long sourceAccountNumber, Long destinationAccountNumber, BigDecimal transferAmount) {
        BankAccountEntity sourceAccount = findAccount(sourceAccountNumber);
        BankAccountEntity destinationAccount = findAccount(destinationAccountNumber);
        transactionService.transfer(sourceAccount,destinationAccount,transferAmount);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
        bankAccountRepository.saveAll(List.of(sourceAccount,destinationAccount));
    }

}
