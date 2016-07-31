package com.zooplus.openexchange.services.security;

public interface CASValidator {
    boolean isValid(String token);
}
