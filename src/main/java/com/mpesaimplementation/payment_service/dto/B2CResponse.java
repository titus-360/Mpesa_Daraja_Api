package com.mpesaimplementation.payment_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Data
public class B2CResponse {
    @JsonProperty("ConversationID")
    private String conversationID;
    @JsonProperty("OriginatorConversationID")
    private String originatorConversationID;
    @JsonProperty("ResponseCode")
    private String responseCode;
    @JsonProperty("ResponseDescription")
    private String responseDescription;
}
