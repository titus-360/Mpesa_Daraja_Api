package com.mpesaimplementation.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Data
public class StkPushResponse {

    @JsonProperty("MerchantRequestID")
    private String MerchantRequestID;
    @JsonProperty("CheckoutRequestID")
    private String CheckoutRequestID;
    @JsonProperty("ResponseCode")
    private String ResponseCode;
    @JsonProperty("ResponseDescription")
    private String ResponseDescription;
    @JsonProperty("CustomerMessage")
    private String CustomerMessage;
}
