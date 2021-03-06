package com.epam.task6.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by olga on 22.04.15.
 */
public class Hasher {
    /* Keeps algorithm name */
    private static final String MD5 = "MD5";

    /**
     * This method calculating MD5 value from string value
     *
     * @param value String value
     * @return MD5 hash of value
     */
    public static String getMD5(String value) {
        try {

            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            byte[] digest = messageDigest.digest(value.getBytes());
            BigInteger number = new BigInteger(1, digest);
            String hash = number.toString(16);
            while (hash.length() < 32) {
                hash = "0" + hash;
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}
