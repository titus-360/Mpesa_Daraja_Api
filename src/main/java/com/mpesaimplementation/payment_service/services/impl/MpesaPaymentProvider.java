package com.mpesaimplementation.payment_service.services.impl;

import com.mpesaimplementation.payment_service.PaymentStatus;
import com.mpesaimplementation.payment_service.config.MpesaConfig;
import com.mpesaimplementation.payment_service.dto.C2BTransactionRequest;
import com.mpesaimplementation.payment_service.dto.C2BTransactionResponse;
import com.mpesaimplementation.payment_service.dto.PaymentResponse;
import com.mpesaimplementation.payment_service.services.MpesaService;
import com.mpesaimplementation.payment_service.services.PaymentProvider;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Service("MPESA")
public class MpesaPaymentProvider implements PaymentProvider {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MpesaPaymentProvider.class);
    private final MpesaConfig mpesaConfig;
    private final MpesaService mpesaService;

    public MpesaPaymentProvider(MpesaConfig mpesaConfig, MpesaService mpesaService) {
        this.mpesaConfig = mpesaConfig;
        this.mpesaService = mpesaService;
    }

    @Override
    public PaymentResponse receivePayment(String amount, String currency, String payer) {
        try {
            // Create C2B transaction request
            C2BTransactionRequest c2bRequest = new C2BTransactionRequest();
            c2bRequest.setShortCode(mpesaConfig.getShortCode());
            c2bRequest.setCommandID("CustomerPayBillOnline");
            c2bRequest.setAmount(amount);
            c2bRequest.setMSisdn(payer);
            c2bRequest.setBillRefNumber("INV12345"); // Replace with an actual reference

            // Simulate the C2B transaction
            C2BTransactionResponse response = mpesaService.simulateC2BTransaction(c2bRequest);
            if (response == null) {
                log.error("MPESA C2B transaction failed for payer: {}", payer);
                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setStatus(PaymentStatus.FAILED);
                paymentResponse.setMessage("Transaction failed");
                return paymentResponse;
            }

            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setStatus(PaymentStatus.COMPLETED);
            paymentResponse.setMessage("Payment received successfully");
            return paymentResponse;
        } catch (IOException e) {
            log.error("Error processing C2B payment: {}", e.getMessage());
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setStatus(PaymentStatus.FAILED);
            paymentResponse.setMessage("Payment processing error");
            return paymentResponse;
        }
    }

    @Override
    public PaymentResponse sendPayment(double amount, String currency, String recipient) {
        return null;
    }

    @Override
    public PaymentStatus checkPaymentStatus(String transactionId) {
        return null;
    }
}