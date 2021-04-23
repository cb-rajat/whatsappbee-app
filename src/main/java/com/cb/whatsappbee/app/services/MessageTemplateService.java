package com.cb.whatsappbee.app.services;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MessageTemplateService {

    private final Map<String, String> templates;

    public MessageTemplateService() {
        templates = new HashMap<>();
        templates.put("invoice_generated", "You have a new invoice! Please view it here: {0}. Reply with PAY to make payment!");
        templates.put("subscription_paused", "Your subscription was successfully paused. Reply with RESUME to resume.");
        templates.put("subscription_paused_error", "Your subscription could not be paused. Please contact U Tees Me support for further help.");
        templates.put("invoice_updated", "You have successfully made payment for ${0} towards your invoice. Thank you!");
        templates.put("invoice_updated_error", "Your payment could not be processed. Please log on to U Tees Me website to try again.");
    }

    public Optional<String> formatTemplate(String eventType, boolean success, String... variables) {
        String templateKey = success ? eventType : eventType + "_error";
        String template = templates.get(templateKey);
        if (template == null) {
            return Optional.empty();
        }
        return Optional.of(MessageFormat.format(template, (Object[]) variables));
    }

    public Optional<String> formatTemplate(String eventType, String... variables) {
        return formatTemplate(eventType, true, variables);
    }

}
