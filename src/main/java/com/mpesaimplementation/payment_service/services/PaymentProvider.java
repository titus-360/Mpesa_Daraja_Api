package com.mpesaimplementation.payment_service.services;

import com.mpesaimplementation.payment_service.PaymentStatus;
import com.mpesaimplementation.payment_service.dto.PaymentResponse;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
public interface PaymentProvider {

    PaymentResponse receivePayment(String amount, String currency, String payer);

    PaymentResponse sendPayment(double amount, String currency, String recipient);

    PaymentStatus checkPaymentStatus(String transactionId);
}
