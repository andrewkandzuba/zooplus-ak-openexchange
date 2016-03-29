package com.zooplus.openexchange.service.security;

import com.zooplus.openexchange.service.security.tokens.AuthenticatedTokenRepository;
import com.zooplus.openexchange.service.security.tokens.TokenService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Optional;

class TokenAuthenticationProvider implements AuthenticationProvider {
    private AuthenticatedTokenRepository authenticatedTokenRepository;

    TokenAuthenticationProvider(AuthenticatedTokenRepository authenticatedTokenRepository) {
        this.authenticatedTokenRepository = authenticatedTokenRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> token = (Optional) authentication.getPrincipal();
        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }
        if (!authenticatedTokenRepository.findByAuthentication(token.get())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }
        return tokenService.retrieve(token.get());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}