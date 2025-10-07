package com.healthexpert.healthbot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final String acc_SID="AC3224a830f63b9feec7d264a3ae1fc5b9";
    private final String acc_TOKEN="1582d9bf64dfc586362339d88568c7e1";

    @PostConstruct
    public void init() {
        System.out.println("++++++initializing twilio++++++");
        Twilio.init(acc_SID, acc_TOKEN);
    }

    public void sendMessage(String to, String message) {
        Twilio.init(acc_SID, acc_TOKEN);
        Message msg =Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + to),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                message
        ).create();
        System.out.println("====="+msg.getSid());
    }

    public void sendSandBoxMessage(String from, String helloThisIsTest) {

    }
}
