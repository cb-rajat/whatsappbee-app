package com.cb.whatsappbee.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChargebeeWebhookService {

    private final MessagingService messagingService;
    private final MessageTemplateService messageTemplateService;
    private final ParserService parserService;
    private final String fromPhoneNumber;

    public ChargebeeWebhookService(@Autowired MessagingService messagingService,
                                   @Autowired MessageTemplateService messageTemplateService,
                                   @Autowired ParserService parserService,
                                   @Value("${prop.phone.from}") String fromPhoneNumber) {
        this.messagingService = messagingService;
        this.messageTemplateService = messageTemplateService;
        this.parserService = parserService;
        this.fromPhoneNumber = fromPhoneNumber;
    }

    public void processAllEvents(JsonNode event) {

        if (!event.has("event_type")) {
            return;
        }

        String eventType = event.get("event_type").asText();
        System.out.println(eventType);
        Optional<String> optContentKey = parserService.getContentKey(eventType);

        if (!optContentKey.isPresent()) {
            return;
        }

        String contentKey = optContentKey.get();
        String templateVariable = getTemplateVariables(eventType, contentKey, event);
        Optional<String> optMessage = messageTemplateService.formatTemplate(eventType, templateVariable);

        optMessage.ifPresent(message -> messagingService.sendMessage(fromPhoneNumber, getCustomerPhoneNumber(event, contentKey), message));

    }

    private String getCustomerPhoneNumber(JsonNode event, String key) {

        return event
                .get("content")
                .get(key)
                .get("billing_address")
                .get("phone")
                .asText();

    }

    private String getTemplateVariables(String eventType, String contentKey, JsonNode event) {

        switch(eventType) {
            case "invoice_updated": {
                return event
                        .get("content")
                        .get(contentKey)
                        .get("amount_paid")
                        .asText();
            }

        }

        return null;
    }

}
