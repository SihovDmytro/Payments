package com.my.payment.util;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashTest {

    @Test
    void hash() throws NoSuchAlgorithmException {
        String hash = PasswordHash.hash("123456");
        assertEquals("8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92",hash);
    }
}