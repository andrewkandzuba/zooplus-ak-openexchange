package com.zooplus.openexchange.services.stubs.security;

import com.zooplus.openexchange.services.security.SecurityTokenValidator;
import org.springframework.stereotype.Component;

@Component
public class LocalTokenValidator implements SecurityTokenValidator {
    @Override
    public boolean isValid(String token) {
        return true;
    }
}
