package com.banksystem.logger;

import com.banksystem.entity.TransactionEntity;
import com.banksystem.entity.TransactionType;
import com.banksystem.logger.observer.TransactionObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TransactionLogger implements TransactionObserver {

    @Override
    public void onTransaction(TransactionEntity transaction) {
        System.out.println("Logging in transaction logger");
        if(transaction.getType().equals(TransactionType.TRANSFER)) {
            log.info("Transaction - " + "Source Account Number : " + transaction.getAccount().getId() + " , Destination Account Number : " + transaction.getTargetAccount().getId() +
                    " , Transaction Type : " + transaction.getType() + " , Amount : " + transaction.getAmount());
        }else {
            log.info("Transaction - " + "Account Number : " + transaction.getAccount().getId() +
                    " ,Transaction Type : " + transaction.getType() + " ,Amount : " + transaction.getAmount());
        }
    }
}
