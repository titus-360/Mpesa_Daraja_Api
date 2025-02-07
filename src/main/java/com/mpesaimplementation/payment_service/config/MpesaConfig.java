package com.mpesaimplementation.payment_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Configuration
@ConfigurationProperties(prefix = "mpesa.daraja")
@Data
public class MpesaConfig {
    private String consumerKey;
    private String consumerSecret;
    private String grantType;
    private String oauthEndpoint;
    private String shortCode;
    private String responseType;
    private String confirmationUrl;
    private String validationUrl;
    private String stkPushCallBack;
    private String urlRegistrationEndpoint;
    private String c2bTransactionEndpoint;
    private String b2cTransactionEndpoint;
    private String stkPushEndpoint;

    public String toString() {
        return "MpesaConfig{" +
                "consumerKey='" + consumerKey + '\'' +
                ", consumerSecret='" + consumerSecret + '\'' +
                ", grantType='" + grantType + '\'' +
                ", oauthTokenUrl='" + oauthEndpoint + '\'' +
                '}';
    }
}
