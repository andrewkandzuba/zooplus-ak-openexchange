package com.zooplus.openexchange.service.data.cache;

import org.springframework.security.core.Authentication;

public interface AuthenticationService {
    void cache(Authentication authentication);
    Authentication get(String token);
    void evict(Authentication authentication);
}
