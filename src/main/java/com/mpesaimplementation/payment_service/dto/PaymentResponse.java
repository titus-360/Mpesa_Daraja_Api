package com.mpesaimplementation.payment_service.dto;

import com.mpesaimplementation.payment_service.PaymentStatus;
import lombok.Data;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
@Data
public class PaymentResponse {
    private String transactionId;
    private PaymentStatus status;
    private String message;
}
