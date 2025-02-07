package com.mpesaimplementation.payment_service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * Author: Titus Murithi Bundi
 * Date:2/7/25
 */
public class HelperUtility {
    private static final Logger log = LoggerFactory.getLogger(HelperUtility.class);

    public static String toBase64(String value) {
        byte[] bytes = value.getBytes(ISO_8859_1);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error occurred: {}", e.getMessage());
        }
        return null;
    }
}
