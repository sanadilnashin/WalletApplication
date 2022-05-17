package com.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    //listen to this topic

    private static final String USER_CREATE_TOPIC="userCreate";
    public static final String TRANSACTION_CREATE_TOPIC="transactionCreate";
    public static final String WALLET_UPDATE_TOPIC="walletUpdate";
    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WalletRepositories walletRepositories;
    //searches for consumer bean//does the mapping
    @KafkaListener(topics = {USER_CREATE_TOPIC},groupId="wallet")
    @KafkaListener(topics = {TRANSACTION_CREATE_TOPIC},groupId="wallet")
    //we have msg that user created
    //till now we havent created wallet entry
    //first need to create wallet
    public void createWallet(String msg) throws JsonProcessingException//msg received from kafka topic
    {
        JSONObject createWalletRequest = objectMapper.readValue(msg, JSONObject.class);
        //got the email and phone number of new request
        String email=(String) createWalletRequest.get("email");
        String phone=(String) createWalletRequest.get("phone");
        //use the value to create new wallet
        Wallet wallet=Wallet.builder()
                .email(email)
                .phone(phone)
                .balance(10.0).build();
        walletRepositories.save(wallet);

    }
    //update detect the amount of one person
    public void updateWallet(String msg) throws JsonProcessingException//msg received from kafka topic
    {
        JSONObject updateWalletRequest = objectMapper.readValue(msg, JSONObject.class);
        //got the email and phone number of new request
        String transactionId=(String) updateWalletRequest.get("transactionId");
        String sender=(String) updateWalletRequest.get("sender");
        String receiver=(String) updateWalletRequest.get("receiver");
        Double amount=(Double) updateWalletRequest.get("amount");
        //using this details we have to find the wallets of sender n receiver
        Wallet senderWallet=walletRepositories.findByEmail(sender);
        Wallet receiverWallet=walletRepositories.findByEmail(receiver);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("transactionId",transactionId);
        jsonObject.put("status","FAILED");
        if (senderWallet.getBalance()>=amount)
        {
            walletRepositories.updateWallet(receiver,amount);
            walletRepositories.updateWallet(sender,0-amount);
            jsonObject.put("status","SUCCESS");
        }

    kafkaTemplate.send(WALLET_UPDATE_TOPIC,objectMapper.writeValueAsString(jsonObject));
    }

    }

