package com.mpesaimplementation.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Data
public class RegisterUrlRequest {
    @JsonProperty("ShortCode")
    private String shortCode;
    @JsonProperty("ResponseType")
    private String responseType;
    @JsonProperty("ConfirmationURL")
    private String confirmationUrl;
    @JsonProperty("ValidationURL")
    private String validationUrl;

}
