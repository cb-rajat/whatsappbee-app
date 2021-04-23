package com.cb.whatsappbee.app.models;

import com.chargebee.models.Subscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SubscriptionMessageActionResponse extends MessageActionResponse {

    @JsonIgnore
    private Subscription subscription;

}
