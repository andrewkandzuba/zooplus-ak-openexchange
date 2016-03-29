package com.zooplus.openexchange.service.security.providers;

import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import com.zooplus.openexchange.service.security.tokens.AuthenticationTokenRepository;
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
    private AuthenticationTokenRepository authenticationTokenRepository;

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
        userAuth.setDetails(authenticationTokenRepository.generateNewToken());
        authenticationTokenRepository.store(userAuth.getDetails().toString(), userAuth);
        return userAuth;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
