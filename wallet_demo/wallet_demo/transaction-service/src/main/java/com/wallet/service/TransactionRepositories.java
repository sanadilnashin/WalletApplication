package com.wallet.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepositories extends JpaRepository<Transaction,Integer> {
    //unsafe query
    @Modifying
    @Query("update Transaction t set t.transactionId=?2 where t.transactionId=?1")
    void updateTransaction(String transactionId,String transactionStatus);
}
