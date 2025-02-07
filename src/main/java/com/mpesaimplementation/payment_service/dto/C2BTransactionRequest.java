package com.mpesaimplementation.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Data
public class C2BTransactionRequest {
    @JsonProperty("ShortCode")
    private String shortCode;
    @JsonProperty("CommandID")
    private String commandID;
    @JsonProperty("Amount")
    private String amount;
    @JsonProperty("Msisdn")
    private String mSisdn;
    @JsonProperty("BillRefNumber")
    private String billRefNumber;
}
