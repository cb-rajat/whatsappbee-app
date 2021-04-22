package com.cb.whatsappbee.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChargebeeWebhookService {

    private final MessagingService messagingService;
    private final MessageTemplateService messageTemplateService;

    public ChargebeeWebhookService(@Autowired MessagingService messagingService,
                                   @Autowired MessageTemplateService messageTemplateService) {
        this.messagingService = messagingService;
        this.messageTemplateService = messageTemplateService;
    }

    public void processAllEvents(JsonNode event) {

        if (!event.has("event_type")) {
            return;
        }

        System.out.println(event.get("event_type"));
        Optional<String> optTemplate = messageTemplateService.getTemplate(event.get("event_type").asText());

        if (!optTemplate.isPresent()) {
            return;
        }

        String template = optTemplate.get();

        messagingService.sendMessage("919953783383", getCustomerPhoneNumber(event, "invoice"), template);

    }

    private String getCustomerPhoneNumber(JsonNode event, String key) {

        return event
                .get("content")
                .get(key)
                .get("billing_address")
                .get("phone")
                .asText();

    }

}
