package com.cb.whatsappbee.app.clients;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class ChargebeeClient {

    private final String chargebeeApiKey;
    private final String baseUrl;

    public ChargebeeClient(@Value("${prop.cb.api.key}") String chargebeeApiKey,
                           @Value("${prop.cb.baseurl}") String baseUrl) {
        this.chargebeeApiKey = chargebeeApiKey;
        this.baseUrl = baseUrl;
    }

    // https://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#list_subscriptions_customer_id
    public void listSubscriptions(String customerId) {

    }

    // https://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#pause_a_subscriptionhttps://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#pause_a_subscription
    public void pauseSubscription(String subscriptionId) {

    }

    // https://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#resume_a_subscription
    public void resumeSubscription(String subscriptionId) {

    }

    // https://apidocs.chargebee.com/docs/api/invoices?prod_cat_ver=1#record_an_invoice_payment
    public void payForInvoice(String invoiceId, BigDecimal amount) {

    }

    // https://apidocs.chargebee.com/docs/api/invoices?prod_cat_ver=1#retrieve_invoice_as_pdf
    public void getInvoicePdf(String invoiceId) {

    }

    // https://apidocs.chargebee.com/docs/api/orders?prod_cat_ver=1#list_orders_subscription_id
    public void checkOrderStatus(String orderId) {

    }

}
