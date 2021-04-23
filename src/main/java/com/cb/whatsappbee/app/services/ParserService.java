package com.cb.whatsappbee.app.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ParserService {

    private final Map<String, String> eventTypeVsKey;

    public ParserService() {
        eventTypeVsKey = new HashMap<>();
        eventTypeVsKey.put("invoice_generated", "invoice");
        eventTypeVsKey.put("invoice_updated", "invoice");
        eventTypeVsKey.put("plan_created", "plan");
    }

    public Optional<String> getContentKey(String eventType) {
        if (!eventTypeVsKey.containsKey(eventType)) {
            return Optional.empty();
        }
        return Optional.of(eventTypeVsKey.get(eventType));
    }

}
