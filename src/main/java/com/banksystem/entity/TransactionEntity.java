package com.banksystem.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity extends AbstractEntity{

    @Column(nullable = false)
    private TransactionType type;

    @Column(precision = 18,nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccountEntity account;

    @ManyToOne
    @JoinColumn(name = "target_account_id", nullable = true)
    private BankAccountEntity targetAccount;

    @PrePersist
    public void setDate(){
        this.date = new Date();
    }
}
