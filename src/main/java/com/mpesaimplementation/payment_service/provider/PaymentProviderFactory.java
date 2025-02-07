package com.mpesaimplementation.payment_service.provider;

import com.mpesaimplementation.payment_service.services.PaymentProvider;
import com.mpesaimplementation.payment_service.services.impl.MpesaPaymentProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Component
public class PaymentProviderFactory {

    private final Map<String, PaymentProvider> providers;

    public PaymentProviderFactory(MpesaPaymentProvider mpesaPaymentProvider) {
        providers = Map.of(
                "MPESA", mpesaPaymentProvider
        );
    }

    public PaymentProvider getProvider(String provider) {
        PaymentProvider paymentProvider = providers.get(provider.toUpperCase());
        if (paymentProvider == null) {
            throw new IllegalArgumentException("Unknown payment provider: " + provider);
        }
        return paymentProvider;
    }
}
