package com.cb.whatsappbee.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChargebeeWebhookService {

    private final MessagingService messagingService;
    private final MessageTemplateService messageTemplateService;
    private final ParserService parserService;

    public ChargebeeWebhookService(@Autowired MessagingService messagingService,
                                   @Autowired MessageTemplateService messageTemplateService,
                                   @Autowired ParserService parserService) {
        this.messagingService = messagingService;
        this.messageTemplateService = messageTemplateService;
        this.parserService = parserService;
    }

    public void processAllEvents(JsonNode event) {

        if (!event.has("event_type")) {
            return;
        }

        String eventType = event.get("event_type").asText();
        System.out.println(eventType);
        Optional<String> optTemplate = messageTemplateService.getTemplate(eventType);
        Optional<String> optContentKey = parserService.getContentKey(eventType);

        if (!optTemplate.isPresent() || !optContentKey.isPresent()) {
            return;
        }

        String template = optTemplate.get();
        String contentKey = optContentKey.get();

        messagingService.sendMessage("919953783383", getCustomerPhoneNumber(event, contentKey), template);

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
