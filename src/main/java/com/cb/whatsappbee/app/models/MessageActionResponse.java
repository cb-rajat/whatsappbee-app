package com.cb.whatsappbee.app.models;

import lombok.Data;

@Data
public class MessageActionResponse {

    private Boolean success;
    private ErrorCode errorCode;
    private String customerPhone;

}
