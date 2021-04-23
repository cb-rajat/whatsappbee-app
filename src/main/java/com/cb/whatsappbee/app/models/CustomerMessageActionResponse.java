package com.cb.whatsappbee.app.models;

import com.chargebee.models.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerMessageActionResponse extends MessageActionResponse {

    private Customer customer;

}
