package com.palvair.security.encryption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class HmacEncrypter {
    private static final String HMAC_ALGO = "HmacSHA256";
    private final String secret;

    @Autowired
    public HmacEncrypter(final @Value("${token.secret}") String secret) {
        this.secret = secret;
    }

    public byte[] createHmac(final byte[] content) {
        try {
            final Mac hmac = Mac.getInstance(HMAC_ALGO);
            final byte[] secretKey = DatatypeConverter.parseBase64Binary(secret);
            hmac.init(new SecretKeySpec(secretKey, HMAC_ALGO));
            return hmac.doFinal(content);
        } catch (final NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("failed to initialize HMAC: " + e.getMessage(), e);
        }
    }

}
