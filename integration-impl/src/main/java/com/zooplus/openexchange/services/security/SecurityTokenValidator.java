package com.zooplus.openexchange.services.security;

public interface SecurityTokenValidator {
    boolean isValid(String token);
}
