package com.wallet.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface WalletRepositories extends JpaRepository<Wallet,Integer> {
    Wallet findByEmail(String email);
    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance=w.balance+:amount where w.email= :email")
    void updateWallet(String email,Double amount);

}
