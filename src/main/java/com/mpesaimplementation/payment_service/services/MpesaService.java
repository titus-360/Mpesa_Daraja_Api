package com.mpesaimplementation.payment_service.services;

import com.mpesaimplementation.payment_service.dto.*;

import java.io.IOException;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
public interface MpesaService {
    AccessTokenResponse getAccessToken();

    RegisterUrlResponse registerUrl();

    C2BTransactionResponse simulateC2BTransaction(C2BTransactionRequest c2BTransactionRequest) throws IOException;

    B2CResponse b2cPaymentRequest(B2CRequest b2CRequest) throws IOException;

    StkPushResponse stkPush(StkPushRequest stkPushRequest) throws IOException;
}
