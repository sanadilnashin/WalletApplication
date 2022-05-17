package com.wallet.service;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private  String transactionId;
    @CreationTimestamp
    private Date createdOn;
    private String sender;
    private Double amount;
    private String receiver;
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;
}
