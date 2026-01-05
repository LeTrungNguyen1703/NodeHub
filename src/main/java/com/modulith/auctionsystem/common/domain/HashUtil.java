package com.modulith.auctionsystem.common.domain;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class for generating hashes for caching and lookups
 */
@UtilityClass
@Slf4j
public class HashUtil {

    /**
     * Generate SHA-256 hash of input string
     * Used for fast lookups in database
     *
     * @param input the string to hash
     * @return Base64 encoded hash
     */
    public static String generateSHA256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.toLowerCase().trim().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not found", e);
            throw new RuntimeException("Failed to generate hash", e);
        }
    }

    /**
     * Normalize word for consistent hashing
     * - Converts to lowercase
     * - Trims whitespace
     *
     * @param word the word to normalize
     * @return normalized word
     */
    public static String normalizeWord(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase();
    }
}

