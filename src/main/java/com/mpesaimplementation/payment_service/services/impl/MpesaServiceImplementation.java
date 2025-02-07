package com.mpesaimplementation.payment_service.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpesaimplementation.payment_service.config.MpesaConfig;
import com.mpesaimplementation.payment_service.dto.*;
import com.mpesaimplementation.payment_service.services.MpesaService;
import com.mpesaimplementation.payment_service.utils.HelperUtility;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Service("MpesaServiceImplementation")
public class MpesaServiceImplementation implements MpesaService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MpesaServiceImplementation.class);

    private final MpesaConfig mpesaConfig;
    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    public MpesaServiceImplementation(MpesaConfig mpesaConfig, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        this.mpesaConfig = mpesaConfig;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccessTokenResponse getAccessToken() {
        String encodedCredentials = HelperUtility.toBase64(mpesaConfig.getConsumerKey() + ":" + mpesaConfig.getConsumerSecret());
        String url = String.format("%s?grant_type=%s", mpesaConfig.getOauthEndpoint(), mpesaConfig.getGrantType());
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Basic " + encodedCredentials)
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending request to URL: {}", url);
        log.info("Authorization header: Basic {}", encodedCredentials);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                //log.error("Request failed with status code: {}", response.code());
                return null;
            }
            String responseBody = response.body().string();
            if (responseBody.isEmpty()) {
                log.error("Received empty response body");
                return null;
            }
            log.info("Received response: {}", responseBody);
            return objectMapper.readValue(responseBody, AccessTokenResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while fetching access token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public RegisterUrlResponse registerUrl() {
        // Get access token
        AccessTokenResponse accessToken = getAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
            return null;
        }
        // Build the request DTO
        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
        registerUrlRequest.setShortCode(mpesaConfig.getShortCode());
        registerUrlRequest.setResponseType(mpesaConfig.getResponseType());
        registerUrlRequest.setConfirmationUrl(mpesaConfig.getConfirmationUrl());
        registerUrlRequest.setValidationUrl(mpesaConfig.getValidationUrl());
        // Serialize the request body to JSON
        String requestBodyJson = HelperUtility.toJson(registerUrlRequest);
        if (requestBodyJson == null) {
            log.error("Failed to serialize request body to JSON");
            return null;
        }
        // Prepare the HTTP request
        RequestBody requestBody = RequestBody.create(requestBodyJson, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(mpesaConfig.getUrlRegistrationEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();

        log.info("Sending register URL request to: {}", mpesaConfig.getUrlRegistrationEndpoint());
        log.info("Request body: {}", requestBodyJson);

        // Execute the request and handle the response
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details provided";
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                log.error("Received empty response body");
                return null;
            }
            log.info("Received response: {}", responseBody);
            return objectMapper.readValue(responseBody, RegisterUrlResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while registering confirmation and validation URLs: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public C2BTransactionResponse simulateC2BTransaction(C2BTransactionRequest c2BTransactionRequest) throws IOException {
        // Get access token
        AccessTokenResponse accessToken = getAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
            return null;
        }
        // Prepare the request body
        RequestBody requestBody = RequestBody.create(Objects.requireNonNull(HelperUtility.toJson(c2BTransactionRequest)), MediaType.get("application/json; charset=utf-8"));
        // Prepare the request
        Request request = new Request.Builder()
                .url(mpesaConfig.getC2bTransactionEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending C2B transaction request to: {}", mpesaConfig.getC2bTransactionEndpoint());
        log.info("Request body: {}", requestBody);
        // Execute the request and handle the response
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No error details provided";
                //log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body() != null ? response.body().string() : "";
            return objectMapper.readValue(responseBody, C2BTransactionResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while simulating C2B transaction: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public B2CResponse b2cPaymentRequest(B2CRequest b2CRequest) throws IOException {
        // Get the access token
        AccessTokenResponse accessToken = getAccessToken();
        if (accessToken == null) {
            log.error("Failed to retrieve access token");
            return null;
        }
        // Prepare the request body
        RequestBody requestBody = RequestBody.create(Objects.requireNonNull(HelperUtility.toJson(b2CRequest)), MediaType.get("application/json; charset=utf-8"));
        // Prepare the request
        Request request = new Request.Builder()
                .url(mpesaConfig.getB2cTransactionEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending B2C payment request to: {}", mpesaConfig.getB2cTransactionEndpoint());
        log.info("Request body: {}", requestBody);
        // Execute the request and handle the response
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, B2CResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while making B2C payment request: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public StkPushResponse stkPush(StkPushRequest stkPushRequest) throws IOException {
        AccessTokenResponse accessToken = getAccessToken();
        if (accessToken == null) {
            return null;
        }
        RequestBody requestBody = RequestBody.create(Objects.requireNonNull(HelperUtility.toJson(stkPushRequest)),
                MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(mpesaConfig.getStkPushEndpoint())
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + accessToken.getAccessToken())
                .addHeader("Content-Type", "application/json")
                .build();
        log.info("Sending STK push request to: {}", mpesaConfig.getStkPushEndpoint());
        log.info("Request body: {}", requestBody);
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body().string();
                log.error("Request failed with status code: {}", response.code());
                log.error("Response body: {}", errorBody);
                return null;
            }
            String responseBody = response.body().string();
            return objectMapper.readValue(responseBody, StkPushResponse.class);
        } catch (IOException e) {
            log.error("Error occurred while making STK push request: {}", e.getMessage());
            return null;
        }
    }
}
