package com.wallet.service;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Wallet {
    //work of wallet is read that message and create a entry in db 10 rs added
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Double balance;
    @Column(unique = true)
    private String email;
    private String phone;
    @CreationTimestamp
    private Date createdOn;
}
