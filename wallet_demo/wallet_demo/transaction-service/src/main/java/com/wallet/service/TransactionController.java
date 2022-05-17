package com.wallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    //whom to who I am sending
    @Autowired
    TransactionService transactionService;
    @PostMapping("/transaction")
    public String doTransaction(@RequestBody TransactionRequest transactionRequest) throws Exception {
        if(!transactionRequest.validate())
        {
            throw new Exception("not valid request!");}
        return transactionService.doTransaction(transactionRequest);
    }
}
