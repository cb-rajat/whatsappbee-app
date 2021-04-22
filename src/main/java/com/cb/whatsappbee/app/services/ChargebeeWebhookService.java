package com.cb.whatsappbee.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class ChargebeeWebhookService {

    public void processAllEvents(JsonNode event) {
        if (event.has("event_type")) {
            System.out.println(event.get("event_type").asText());
        }
    }
}
