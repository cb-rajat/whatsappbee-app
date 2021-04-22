package com.cb.whatsappbee.app.services;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageTemplateService {

    public Optional<String> getTemplate(String eventType) {
        return Optional.empty();
    }
}
