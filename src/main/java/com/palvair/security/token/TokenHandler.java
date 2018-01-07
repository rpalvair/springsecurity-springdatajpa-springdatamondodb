package com.palvair.security.token;

import com.palvair.domain.UserService;
import com.palvair.security.encryption.HmacEncrypter;
import com.palvair.security.model.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

import static com.palvair.tools.Base64Utils.fromBase64;
import static com.palvair.tools.Base64Utils.toBase64;


@Log4j
@Component
public class TokenHandler {

    private static final String SEPARATOR = ".";
    private static final String SEPARATOR_SPLITTER = "\\.";

    private final UserService userService;
    private final HmacEncrypter hmacEncrypter;

    @Autowired
    public TokenHandler(final UserService userService,
                        final HmacEncrypter hmacEncrypter) {
        this.userService = userService;
        this.hmacEncrypter = hmacEncrypter;
    }

    public User parseUserFromToken(final String token) {
        final String[] parts = token.split(SEPARATOR_SPLITTER);
        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0) {
            try {
                final byte[] userBytes = fromBase64(parts[0]);
                final byte[] hash = fromBase64(parts[1]);

                boolean validHash = Arrays.equals(hmacEncrypter.createHmac(userBytes), hash);
                if (validHash) {
                    final User user = userService.fromJSON(userBytes);
                    if (new Date().getTime() < user.getExpires()) {
                        return user;
                    }
                }
            } catch (IllegalArgumentException e) {
                log.error(e);
                throw e;
            }
        }
        return null;
    }

    public String createTokenForUser(User user) {
        byte[] userBytes = userService.toJSON(user);
        byte[] hash = hmacEncrypter.createHmac(userBytes);
        return toBase64(userBytes) + SEPARATOR + toBase64(hash);
    }


}
