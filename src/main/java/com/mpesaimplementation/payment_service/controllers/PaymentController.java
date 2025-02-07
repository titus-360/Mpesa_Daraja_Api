package com.mpesaimplementation.payment_service.controllers;

import com.mpesaimplementation.payment_service.dto.PaymentResponse;
import com.mpesaimplementation.payment_service.dto.StkPushRequest;
import com.mpesaimplementation.payment_service.dto.StkPushResponse;
import com.mpesaimplementation.payment_service.services.MpesaService;
import com.mpesaimplementation.payment_service.services.impl.MpesaServiceImplementation;
import com.mpesaimplementation.payment_service.services.impl.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final MpesaService mpesaService;

    public PaymentController(PaymentService paymentService, MpesaService mpesaService) {
        this.paymentService = paymentService;
        this.mpesaService = mpesaService;
    }

    @PostMapping("/{provider}")
    public ResponseEntity<?> makePayment(
            @PathVariable String provider,
            @RequestParam(required = false) String amount,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String payer,
            @RequestBody(required = false) StkPushRequest stkPushRequest
    ) {
        if ("MPESA".equalsIgnoreCase(provider) && stkPushRequest != null) {
            try {
                StkPushResponse response = mpesaService.stkPush(stkPushRequest);
                if (response != null) {
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("STK push failed.");
                }
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing STK push.");
            }
        }

        // Handle normal payments
        if (amount == null || currency == null || payer == null) {
            return ResponseEntity.badRequest().body("Missing required payment parameters.");
        }
        PaymentResponse response = paymentService.processPayment(provider, amount, currency, payer);
        return ResponseEntity.ok(response);
    }

}
