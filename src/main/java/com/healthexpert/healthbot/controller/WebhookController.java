package com.healthexpert.healthbot.controller;

import com.healthexpert.healthbot.entity.PatientCase;
import com.healthexpert.healthbot.repository.HealthCenterRepository;
import com.healthexpert.healthbot.repository.PatientCaseRepository;
import com.healthexpert.healthbot.service.WhatsAppBotService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/whatsapp")
public class WebhookController {

    private final WhatsAppBotService botService;

    public WebhookController(WhatsAppBotService botService) {
        this.botService = botService;
    }

    @GetMapping("/test")
    public String testApi(){
        return "api working";
    }

    @PostMapping("/webhook")
    public void receiveMessage(@RequestParam("From") String from,
                               @RequestParam("Body") String body) {
        botService.handleIncommingMessage(from,body);
    }
}
