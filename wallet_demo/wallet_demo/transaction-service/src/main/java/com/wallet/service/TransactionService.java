package com.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    TransactionRepositories transactionRepositories;
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;
    public static final String TRANSACTION_CREATE_TOPIC="transactionCreate";
    public static final String WALLET_UPDATE_TOPIC="walletUpdate";
    public String doTransaction(TransactionRequest transactionRequest) throws JsonProcessingException {
        // built transaction object
        Transaction transaction=new Transaction();
        transaction.builder()
                .transactionId(UUID.randomUUID().toString())
                .receiver(transactionRequest.getReceiver())
                .sender(transactionRequest.getSender())
                .transactionStatus(TransactionStatus.PENDING)
                .amount(transactionRequest.getAmount())
                .build();
        transactionRepositories.save(transaction);
        //creation of msg
        JSONObject transactionCreate=new JSONObject();
        transactionCreate.put("transactionId",transaction.getTransactionId());
        transactionCreate.put("sender",transaction.getSender());
        transactionCreate.put("receiver",transaction.getReceiver());
        transactionCreate.put("amount",transaction.getAmount());
        kafkaTemplate.send(TRANSACTION_CREATE_TOPIC,objectMapper.writeValueAsString(transactionCreate));

    return transaction.getTransactionId();
    }
    @KafkaListener(topics = {WALLET_UPDATE_TOPIC},groupId="wallet")
    public void updateTransaction(String msg) throws JsonProcessingException {
        //read value
        JSONObject updateWallet= objectMapper.readValue(msg, JSONObject.class);
        //get the transaction id
        String transactionId=(String) updateWallet.get("transactionId");
        //status
        String status=(String) updateWallet.get("status");
      //  String SUCCESS=(String) updateTransaction.get("SUCCESS");
        transactionRepositories.updateTransaction(transactionId,status);

    }
}
