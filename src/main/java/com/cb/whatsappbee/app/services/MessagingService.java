package com.cb.whatsappbee.app.services;

import com.cb.whatsappbee.app.clients.ChargebeeClient;
import com.cb.whatsappbee.app.models.MessageActionResponse;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessagingService {

    private final ChargebeeClient chargebeeClient;
    private final MessageTemplateService messageTemplateService;
    private final String from;
    private final String messagingServiceUrl;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

    public MessagingService(@Autowired ChargebeeClient chargebeeClient,
                            @Autowired MessageTemplateService messageTemplateService,
                            @Value("${prop.phone.from}") String from,
                            @Value("${prop.messaging.url}") String messagingServiceUrl) {
        this.chargebeeClient = chargebeeClient;
        this.messageTemplateService = messageTemplateService;
        this.messagingServiceUrl = messagingServiceUrl;
        this.from = from;
    }

    public void sendMessage(String from, String to, String message) {

        try {
            String requestBody =
                    "from_whatsapp_number=" + from +
                            "&to_whatsapp_number=" + to +
                            "&txt_msg=" + message;
            RequestBody body = RequestBody.create(requestBody, mediaType);
            System.out.println(requestBody);
            Request request = new Request.Builder()
                    .url(messagingServiceUrl)
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.code());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public MessageActionResponse processMessage(String from, String message) {

        switch(message) {
            case "PAUSE": {
                MessageActionResponse response = chargebeeClient.pauseSubscription(from);
                if (!response.getSuccess()) {
                    sendMessageForErrorCode(response.getErrorCode().name().toLowerCase(), from);
                }
                return response;
            }

            case "RESUME": {
                MessageActionResponse response = chargebeeClient.resumeSubscription(from);
                if(!response.getSuccess()) {
                    sendMessageForErrorCode(response.getErrorCode().name().toLowerCase(), from);
                }
                return response;
            }

            case "PAY": {
                MessageActionResponse response = chargebeeClient.payForInvoice(from);
                if(!response.getSuccess()) {
                    sendMessageForErrorCode(response.getErrorCode().name().toLowerCase(), from);
                }
                return response;
            }

        }

        return null;
    }

    private void sendMessageForErrorCode(String errorCode, String to) {

        Optional<String> optMessage = messageTemplateService.formatTemplate(errorCode);
        optMessage.ifPresent(message -> sendMessage(from, to, message));

    }


}
