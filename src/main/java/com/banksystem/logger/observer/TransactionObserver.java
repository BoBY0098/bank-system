package com.banksystem.logger.observer;

import com.banksystem.entity.TransactionEntity;


public interface TransactionObserver {
    void onTransaction(TransactionEntity transaction);
}
