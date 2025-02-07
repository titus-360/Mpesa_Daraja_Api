package com.mpesaimplementation.payment_service.services.impl;

import com.mpesaimplementation.payment_service.PaymentStatus;
import com.mpesaimplementation.payment_service.dto.PaymentResponse;
import com.mpesaimplementation.payment_service.provider.PaymentProviderFactory;
import com.mpesaimplementation.payment_service.services.PaymentProvider;
import org.springframework.stereotype.Service;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Service("paymentService")
public class PaymentService {

    private final PaymentProviderFactory paymentProviderFactory;

    public PaymentService(PaymentProviderFactory paymentProviderFactory) {
        this.paymentProviderFactory = paymentProviderFactory;
    }

    public PaymentResponse processPayment(String provider, String amount, String currency, String payer) {
        try {
            // Get the correct provider dynamically
            PaymentProvider paymentProvider = paymentProviderFactory.getProvider(provider);

            // Call receivePayment on the selected provider
            return paymentProvider.receivePayment(amount, currency, payer);
        } catch (IllegalArgumentException e) {
            PaymentResponse response = new PaymentResponse();
            response.setStatus(PaymentStatus.FAILED);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
