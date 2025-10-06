package com.healthexpert.healthbot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendMessage(String to, String message) {
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber("whatsapp:" + fromNumber),
                message
        ).create();
    }

    public void sendSandBoxMessage(String from, String helloThisIsTest) {
        String acc_SID="AC3224a830f63b9feec7d264a3ae1fc5b9";
        String acc_TOKEN="21b970fd8376c218b33d9dd5bb25acf0";

        Twilio.init(acc_SID,acc_TOKEN);
        Message msg = Message.creator(
        new com.twilio.type.PhoneNumber("whatsapp:+918096141026"),
        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
        helloThisIsTest).create();

        System.out.println("====="+msg.getSid());

    }
}
