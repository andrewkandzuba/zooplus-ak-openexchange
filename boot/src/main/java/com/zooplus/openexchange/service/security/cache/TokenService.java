package com.zooplus.openexchange.service.security.cache;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface TokenService {
    Authentication issue(String userName, Collection<? extends GrantedAuthority> authorities);
}
