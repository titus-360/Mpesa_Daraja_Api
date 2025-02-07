package com.mpesaimplementation.payment_service.controllers;

import com.mpesaimplementation.payment_service.dto.*;
import com.mpesaimplementation.payment_service.services.MpesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@RestController
@RequestMapping("/daraja")
public class MpesaController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MpesaController.class);
    private final MpesaService mpesaService;

    public MpesaController(MpesaService mpesaService) {
        this.mpesaService = mpesaService;
    }

    @GetMapping(value = "/access-token", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccessTokenResponse> getAccessToken() {
        try {
            return ResponseEntity.ok(mpesaService.getAccessToken());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/register-url", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RegisterUrlResponse> registerUrl() {
        try {
            return ResponseEntity.ok(mpesaService.registerUrl());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/validation", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<AcknowledgementResponse> validation(@Valid @RequestBody MpesaValidationResponse mpesaValidationResponse) {
        try {
            log.info("Received validation request: {}", mpesaValidationResponse);
            AcknowledgementResponse acknowledgementResponse = new AcknowledgementResponse();
            return ResponseEntity.ok(acknowledgementResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "/confirmation", produces = "application/json")
    public ResponseEntity<String> handleConfirmation(@Valid @RequestBody ConfirmationRequest confirmationRequest) {
        try {
            log.info("Received confirmation request: {}", confirmationRequest);
            return ResponseEntity.ok("Confirmation received successfully");
        } catch (Exception e) {
            log.error("Error processing confirmation request: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error processing confirmation request");
        }
    }

    @PostMapping(value = "c2b/simulate", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<C2BTransactionResponse> simulateC2BTransaction(@Valid @RequestBody C2BTransactionRequest c2BTransactionRequest) {
        try {
            return ResponseEntity.ok(mpesaService.simulateC2BTransaction(c2BTransactionRequest));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "b2c/payment-request", produces = "application/json")
    public ResponseEntity<B2CResponse> b2cPaymentRequest(@RequestBody B2CRequest b2CRequest) {
        try{
            return ResponseEntity.ok(mpesaService.b2cPaymentRequest(b2CRequest));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "stk/push", produces = "application/json")
    public ResponseEntity<StkPushResponse> stkPush(@Valid @RequestBody StkPushRequest stkPushRequest) {
        try{
            return ResponseEntity.ok(mpesaService.stkPush(stkPushRequest));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "stkpush/callback", produces = "application/json")
    public ResponseEntity<AcknowledgementResponse> stkPushFallback(@Valid @RequestBody StkPushCallback stkPushCallback) {
        try {
            log.info("Received STK Push callback: {}", stkPushCallback);

            if (stkPushCallback.getResultCode() == 1032) {
                log.info("User canceled the STK Push request.");
            } else {
                log.info("STK Push request completed with result code: {}", stkPushCallback.getResultCode());
            }
            AcknowledgementResponse acknowledgementResponse = new AcknowledgementResponse();
            return ResponseEntity.ok(acknowledgementResponse);
        } catch (Exception e) {
            log.error("Error processing STK Push callback: {}", e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }
}