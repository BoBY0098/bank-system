package com.banksystem.ui;

import com.banksystem.entity.BankAccountEntity;
import com.banksystem.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

@Component
@Slf4j
@RequiredArgsConstructor
public class UiConsole implements CommandLineRunner {

    private final BankService bankService;
    private final ExecutorService executorService;

    @Override
    public void run(String... args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down ExecutorService...");
            executorService.shutdown();
        }));

        Scanner scanner = new Scanner(System.in);


        while (true) {
            introductions();
            int option = scanner.nextInt();

            try {
                switch (option) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = scanner.next();

                        System.out.print("Enter initial balance: ");
                        BigDecimal initialBalance = scanner.nextBigDecimal();

                        BankAccountEntity account = bankService.createAccount(name, initialBalance);
                        System.out.println("Account created for : " + account.getName() + " with account Number : " + account.getId() +
                                " (Balance : " + account.getBalance() + ")");
                        break;

                    case 2:
                        System.out.print("Enter Account Number: ");
                        Long accountNumberDeposit = scanner.nextLong();

                        System.out.print("Enter amount to deposit: ");
                        BigDecimal depositAmount = scanner.nextBigDecimal();

                        bankService.deposit(accountNumberDeposit, depositAmount);
                        System.out.println("Account balance increased for : " + depositAmount + " with account Number : " + accountNumberDeposit);
                        break;

                    case 3:
                        System.out.print("Enter Account Number: ");
                        Long accountNumberWithdraw = scanner.nextLong();

                        System.out.print("Enter amount to withdraw: ");
                        BigDecimal withdrawAmount = scanner.nextBigDecimal();

                        bankService.withdraw(accountNumberWithdraw, withdrawAmount);
                        System.out.println("Account balance decreased for : " + withdrawAmount + " with account Number : " + accountNumberWithdraw);
                        break;

                    case 4:
                        System.out.print("Enter Source Account Number: ");
                        Long sourceAccountNumber = scanner.nextLong();

                        System.out.print("Enter Destination Account Number: ");
                        Long destinationAccountNumber = scanner.nextLong();

                        System.out.print("Enter amount to transfer: ");
                        BigDecimal transferAmount = scanner.nextBigDecimal();

                        bankService.transfer(sourceAccountNumber, destinationAccountNumber, transferAmount);
                        System.out.println("Transfer done from account: " + sourceAccountNumber + " to account: " + destinationAccountNumber +
                                " In the amount of: " + transferAmount);
                        break;

                    case 5:
                        System.out.print("Enter Account Number: ");
                        Long accountNumber = scanner.nextLong();

                        BigDecimal balance = bankService.getBalance(accountNumber);
                        System.out.println("Account balance is: " + balance + " for account Number : " + accountNumber);
                        break;

                    case 6:
                        scanner.close();
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                log.error("An error occurred: ", e);
            }
        }
    }

    private static void introductions() {
        System.out.println("1. Create Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Check Balance");
        System.out.println("6. Exit");
        System.out.println("Choose an option: ");
    }
}
