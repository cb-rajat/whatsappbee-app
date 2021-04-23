package com.cb.whatsappbee.app.models;

import com.chargebee.models.Invoice;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvoiceMessageActionResponse extends MessageActionResponse {

    @JsonIgnore
    private Invoice invoice;

}
