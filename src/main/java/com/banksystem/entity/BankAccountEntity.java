package com.banksystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountEntity extends AbstractEntity{

    @Column(precision = 18,nullable = false)
    private BigDecimal balance;

    @Column(length = 100,nullable = false)
    private String name;

}
