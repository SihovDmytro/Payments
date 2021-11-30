package com.my.payment.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class contains functionality that hashes passwords
 * @author Sihov Dmytro
 */
public class PasswordHash {
    private static final Logger logger = LogManager.getLogger(PasswordHash.class);

    public static String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(input.getBytes());
        StringBuilder output = new StringBuilder();
        byte[] hash = digest.digest();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xFF & hash[i]);
            if (hex.length() == 1) {
                output.append('0');
            }
            output.append(hex);
        }
        logger.trace("password after hash() ==> " + output.toString().toUpperCase());
        return output.toString().toUpperCase();
    }
}
