package com.cb.whatsappbee.app.services;

import com.cb.whatsappbee.app.clients.ChargebeeClient;
import com.cb.whatsappbee.app.models.MessageActionResponse;
import com.cb.whatsappbee.app.models.SubscriptionMessageActionResponse;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final ChargebeeClient chargebeeClient;
    private final String messagingServiceUrl;
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

    public MessagingService(@Autowired ChargebeeClient chargebeeClient,
                            @Value("${prop.messaging.url}") String messagingServiceUrl) {
        this.chargebeeClient = chargebeeClient;
        this.messagingServiceUrl = messagingServiceUrl;
    }

    public void sendMessage(String from, String to, String message) {

        try {
            String requestBody =
                    "from_whatsapp_number=" + from +
                            "&to_whatsapp_number=" + to +
                            "&txt_msg=" + message;
            RequestBody body = RequestBody.create(requestBody, mediaType);
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
                chargebeeClient.pauseSubscription(from);
            }

            case "RESUME": {
                return chargebeeClient.resumeSubscription(from);
            }

            case "PAY": {

            }

        }

        return null;
    }


}
