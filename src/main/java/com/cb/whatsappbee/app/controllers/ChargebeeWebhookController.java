package com.cb.whatsappbee.app.controllers;

import com.cb.whatsappbee.app.services.ChargebeeWebhookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChargebeeWebhookController {

    private final ChargebeeWebhookService chargebeeWebhookService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChargebeeWebhookController(@Autowired ChargebeeWebhookService chargebeeWebhookService) {
        this.chargebeeWebhookService = chargebeeWebhookService;
    }

    @PostMapping("/events")
    public String allEvents(@RequestBody String webhook) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(webhook);
        chargebeeWebhookService.processAllEvents(jsonNode);
        return "ok";
    }
}
