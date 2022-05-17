package com.wallet.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepositories userRepositories;
    @Autowired
    KafkaTemplate kafkaTemplate;
    //data type String
    @Autowired
    ObjectMapper objectMapper;
    //topic
    private static final String USER_CREATE_TOPIC="userCreate";
    public User getUserByEmail(String email)
    {
        return userRepositories.findByEmail(email);
    }
    //whenever a new user joins our application we are going to add money in their wallet
    public void createUser(User user) throws JsonProcessingException {
        //calling user rep to save
        userRepositories.save(user);
        //send a message to kafka that hey we have created a user.for that we need a topic
        //user is type of json so we are using json obj to get only email & phone.
        //our message has been constructed
        JSONObject userRequest=new JSONObject();
        userRequest.put("email",user.getEmail());
        userRequest.put("phone",user.getPhone());
        //so we need to send this message to kafka queue
        //to my kafka template we will send a msg to this topic and using obj mapper we will write value as string to this user request
        kafkaTemplate.send(USER_CREATE_TOPIC,objectMapper.writeValueAsString(userRequest));
    }
}
