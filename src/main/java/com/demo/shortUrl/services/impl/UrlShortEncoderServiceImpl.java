package com.demo.shortUrl.services.impl;

import com.demo.shortUrl.services.UrlShortEncoderService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class UrlShortEncoderServiceImpl implements UrlShortEncoderService {
    @Override
    public String encodeUrl(final String encode) {
        String encodedUrl = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
            byte[] salt = getSalt();
            messageDigest.update(salt);
            byte[] encodedhash = messageDigest.digest(
                    encode.getBytes(StandardCharsets.UTF_8));
            encodedUrl = Base64.getEncoder().encodeToString(encodedhash).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error, not possible to generate an encodedUrl");
        }
        return encodedUrl;
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
