package com.zooplus.openexchange.service.security;

import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.security.tokens.AuthenticatedToken;
import com.zooplus.openexchange.service.security.tokens.AuthenticatedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSourceAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticatedTokenRepository authenticatedTokenRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();
        if(!username.isPresent() && !password.isPresent()){
            throw new BadCredentialsException("Empty credentials");
        }
        User user = userRepository.findByNameAndPassword(username.get(), password.get());
        if(user == null){
            throw new BadCredentialsException("Invalid credentials");
        }

        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(user.getName(), null, user.getRoles());
        AuthenticatedToken newToken = authenticatedTokenRepository.findByAuthentication(userAuth);
        userAuth.setDetails(newToken.getToken());
        return userAuth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
