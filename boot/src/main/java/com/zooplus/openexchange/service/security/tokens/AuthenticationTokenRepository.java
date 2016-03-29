package com.zooplus.openexchange.service.security.tokens;


import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AuthenticationTokenRepository {
    void store(String token, Authentication authentication);
    Authentication findByToken(String token);
    default String generateNewToken() {
        return UUID.randomUUID().toString();
    }
}
