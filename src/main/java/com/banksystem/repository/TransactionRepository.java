package com.banksystem.repository;

import com.banksystem.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {

    @Query("""
        select SUM(
            CASE
            WHEN te.type=com.banksystem.entity.TransactionType.DEPOSIT THEN te.amount
            WHEN te.type=com.banksystem.entity.TransactionType.WITHDRAW THEN -te.amount
            WHEN te.type=com.banksystem.entity.TransactionType.TRANSFER and te.account.id=:aid THEN -te.amount
            WHEN te.type=com.banksystem.entity.TransactionType.TRANSFER and te.targetAccount.id=:aid THEN te.amount
            ELSE 0 END
        ) from TransactionEntity te where te.account.id=:aid or te.targetAccount.id=:aid
""")
    BigDecimal getBalanceFromTransactions(@Param("aid") Long accountId);
}
