package com.cb.whatsappbee.app.clients;

import com.cb.whatsappbee.app.models.*;
import com.chargebee.Environment;
import com.chargebee.ListResult;
import com.chargebee.Result;
import com.chargebee.models.Customer;
import com.chargebee.models.Download;
import com.chargebee.models.Invoice;
import com.chargebee.models.Subscription;
import com.chargebee.models.enums.PauseOption;
import com.chargebee.models.enums.PaymentMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Component
public class ChargebeeClient {



    public ChargebeeClient(@Value("${prop.cb.api.key}") String chargebeeApiKey,
                           @Value("${prop.cb.sitename}") String chargebeeSiteName) {
        Environment.configure(chargebeeSiteName, chargebeeApiKey);
    }

    public CustomerMessageActionResponse fetchCustomer(String phoneNumber) {

        CustomerMessageActionResponse response = new CustomerMessageActionResponse();
        response.setSuccess(false);

        try {
            ListResult customers = Customer.list()
                    .phone().is(phoneNumber)
                    .limit(1)
                    .request();
            if (customers.size() < 1) {
                response.setErrorCode(ErrorCode.CUSTOMER_NOT_FOUND);
                return response;
            }

            response.setSuccess(true);
            response.setCustomer(customers.get(0).customer());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setErrorCode(ErrorCode.CHARGEBEE_ERROR);
        return response;
    }

    // https://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#pause_a_subscriptionhttps://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#pause_a_subscription
    public SubscriptionMessageActionResponse pauseSubscription(String phoneNumber) {

        SubscriptionMessageActionResponse response = new SubscriptionMessageActionResponse();
        response.setSuccess(false);

        try {
            CustomerMessageActionResponse customerResponse = fetchCustomer(phoneNumber);

            if (!customerResponse.getSuccess()) {
                response.setErrorCode(customerResponse.getErrorCode());
                return response;
            }

            String customerId = customerResponse.getCustomer().id();

            SubscriptionMessageActionResponse subscriptionResponse = fetchSubscriptionByCustomerId(customerId, Subscription.Status.ACTIVE);

            if (!subscriptionResponse.getSuccess()) {
                response.setErrorCode(subscriptionResponse.getErrorCode());
                return response;
            }

            Result pauseResult = Subscription.pause(subscriptionResponse.getSubscription().id())
                    .pauseOption(PauseOption.IMMEDIATELY)
                    .request();

            if (pauseResult.httpCode() == 200) {
                response.setSuccess(true);
                response.setSubscription(pauseResult.subscription());
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setErrorCode(ErrorCode.CHARGEBEE_ERROR);
        return response;

    }

    // https://apidocs.chargebee.com/docs/api/subscriptions?prod_cat_ver=1#resume_a_subscription
    public SubscriptionMessageActionResponse resumeSubscription(String phoneNumber) {
        SubscriptionMessageActionResponse response = new SubscriptionMessageActionResponse();
        response.setSuccess(false);

        try {
            CustomerMessageActionResponse customerResponse = fetchCustomer(phoneNumber);

            if (!customerResponse.getSuccess()) {
                response.setErrorCode(customerResponse.getErrorCode());
                return response;
            }

            String customerId = customerResponse.getCustomer().id();

            SubscriptionMessageActionResponse subscriptionResponse = fetchSubscriptionByCustomerId(customerId, Subscription.Status.PAUSED);

            if (!subscriptionResponse.getSuccess()) {
                response.setErrorCode(subscriptionResponse.getErrorCode());
                return response;
            }

            Result resumeResult = Subscription.resume(subscriptionResponse.getSubscription().id())
                    .request();

            if (resumeResult.httpCode() == 200) {
                response.setSuccess(true);
                response.setSubscription(resumeResult.subscription());
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setErrorCode(ErrorCode.CHARGEBEE_ERROR);
        return response;
    }

    // https://apidocs.chargebee.com/docs/api/invoices?prod_cat_ver=1#record_an_invoice_payment
    public InvoiceMessageActionResponse payForInvoice(String phoneNumber) {

        InvoiceMessageActionResponse response = new InvoiceMessageActionResponse();
        response.setSuccess(false);

        try {
            CustomerMessageActionResponse customerResponse = fetchCustomer(phoneNumber);

            if (!customerResponse.getSuccess()) {
                response.setErrorCode(customerResponse.getErrorCode());
                return response;
            }

            String customerId = customerResponse.getCustomer().id();

            InvoiceMessageActionResponse invoiceResponse = fetchInvoiceByCustomerId(customerId);

            if (!invoiceResponse.getSuccess()) {
                response.setErrorCode(invoiceResponse.getErrorCode());
                return response;
            }

            Result payResult = Invoice.recordPayment(invoiceResponse.getInvoice().id())
                    .comment("Paid via WhatsApp")
                    .transactionAmount(invoiceResponse.getInvoice().amountDue())
                    .transactionPaymentMethod(PaymentMethod.CASH)
                    .transactionDate(new Timestamp(new Date().getTime()))
                    .request();

            if (payResult.httpCode() == 200) {
                response.setSuccess(true);
                response.setInvoice(payResult.invoice());
                return response;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setErrorCode(ErrorCode.CHARGEBEE_ERROR);
        return response;

    }

    // https://apidocs.chargebee.com/docs/api/invoices?prod_cat_ver=1#retrieve_invoice_as_pdf
    public String getInvoicePdf(String invoiceId) {

        try {
            Result result = Invoice.pdf(invoiceId).request();
            if (result.httpCode() == 200) {
                Download download = result.download();
                return download.downloadUrl();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // https://apidocs.chargebee.com/docs/api/orders?prod_cat_ver=1#list_orders_subscription_id
    public void checkOrderStatus(String orderId) {

    }

    private SubscriptionMessageActionResponse fetchSubscriptionByCustomerId(String customerId, Subscription.Status status) {

        SubscriptionMessageActionResponse response = new SubscriptionMessageActionResponse();

        try {

            ListResult subscriptions = Subscription.list()
                    .status().is(status)
                    .customerId().is(customerId)
                    .limit(1)
                    .request();

            if (subscriptions.size() < 1) {
                response.setSuccess(false);
                response.setErrorCode(ErrorCode.SUBSCRIPTION_NOT_FOUND);
                return response;
            }

            response.setSuccess(true);
            response.setSubscription(subscriptions.get(0).subscription());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setSuccess(false);
        response.setErrorCode(ErrorCode.CHARGEBEE_ERROR);
        return response;
    }

    private InvoiceMessageActionResponse fetchInvoiceByCustomerId(String customerId) {

        InvoiceMessageActionResponse response = new InvoiceMessageActionResponse();

        try {

            ListResult invoices = Invoice.list()
                    .status().in(Invoice.Status.PAYMENT_DUE, Invoice.Status.NOT_PAID)
                    .customerId().is(customerId)
                    .limit(1)
                    .request();

            if (invoices.size() < 1) {
                response.setSuccess(false);
                response.setErrorCode(ErrorCode.INVOICE_NOT_FOUND);
                return response;
            }

            response.setSuccess(true);
            response.setInvoice(invoices.get(0).invoice());
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setSuccess(false);
        response.setErrorCode(ErrorCode.CHARGEBEE_ERROR);
        return response;

    }

}
