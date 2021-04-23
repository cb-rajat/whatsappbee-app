package com.cb.whatsappbee.app.controllers;

import com.cb.whatsappbee.app.models.MessageActionResponse;
import com.cb.whatsappbee.app.models.WhatsappMessage;
import com.cb.whatsappbee.app.services.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/message")
public class MessagingController {

    private final MessagingService messagingService;

    public MessagingController(@Autowired MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping
    public MessageActionResponse receiveMessage(@RequestBody WhatsappMessage message) {
        return messagingService.processMessage(message.getFrom(), message.getMessage());
    }

}
